package com.coinnote.entryservice.service;

import com.coinnote.entryservice.components.security.KeycloakComms.KeycloakAdminService;
import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.AccountingTypes;
import com.coinnote.entryservice.entity.CommonInstance;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import com.coinnote.entryservice.mapper.DataMapper;
import com.coinnote.entryservice.repository.CommonInstanceMongoRepository;
import com.coinnote.entryservice.service.history.HistoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public abstract class DataUpdateService<I extends CommonInstance, D extends CommonDto> {

    @Autowired
    protected CommonInstanceMongoRepository<I> commonInstanceRepository;
    @Autowired
    protected HistoryClient historyClient;
    @Autowired
    protected DataMapper dataMapper;
    @Autowired
    protected KeycloakAdminService keycloakAdminService;

    /*
    public I mapDtoToInstance(D dto){
        //TODO:get user
        User user = new User();
        return dataMapper.mapDtoToInstance(dto, user);
    }

    public D mapInstanceToDto(I instance){
        return dataMapper.mapInstanceToDto(instance);
    }
    */

    @Transactional
    public abstract D saveAsInstance(D dto);
    @Transactional
    public abstract void updateAsInstance(D dto);
    @Transactional
    public abstract D getAsDto(Long id, String period);
    @Transactional
    public abstract D getAsDto(String id, String period);


    //TODO: implement this
    protected void checkAndResetIfTimeExpired(I instance, List<HashMap<String, Long>> modifyHashMaps){

        boolean isUpdateRequiredWeek = false;
        boolean isUpdateRequiredMonth = false;
        boolean isUpdateRequiredYear = false;

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime editedAt = instance.getEditedAt();
        LocalDateTime lastOfWeek = editedAt.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDateTime lastOfMonth = editedAt.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime lastOfYear = editedAt.with(TemporalAdjusters.lastDayOfYear());

        Long timeSpanSinceEdited = ChronoUnit.MINUTES.between(current, editedAt);

        //If current moment exceeds the time passed between "editedAt" and respective lastOf_X date,
        //then we shall update parameters
        if (timeSpanSinceEdited - ChronoUnit.MINUTES.between(current, lastOfWeek) >= 0){
            isUpdateRequiredWeek = true;
        };

        if (timeSpanSinceEdited - ChronoUnit.MINUTES.between(current, lastOfMonth) >= 0){
            isUpdateRequiredMonth = true;
        }

        if (timeSpanSinceEdited - ChronoUnit.MINUTES.between(current, lastOfYear) >= 0){
            isUpdateRequiredYear = true;
        }

        if (isUpdateRequiredWeek && isUpdateRequiredMonth && isUpdateRequiredYear) {
            modifyHashMaps.parallelStream().forEach(temp -> {
                temp.put(AccountingTypes.w.toString(), 0L);
                temp.put(AccountingTypes.m.toString(), 0L);
                temp.put(AccountingTypes.y.toString(), 0L);
            });

            instance.setEditedAt(current);
        }else if (isUpdateRequiredWeek && isUpdateRequiredMonth){
            modifyHashMaps.parallelStream().forEach(temp -> {
                temp.put(AccountingTypes.w.toString(), 0L);
                temp.put(AccountingTypes.m.toString(), 0L);
            });

            instance.setEditedAt(current);
        }else if (isUpdateRequiredWeek){
            modifyHashMaps.parallelStream().forEach(temp -> {
                temp.put(AccountingTypes.w.toString(), 0L);
            });

            instance.setEditedAt(current);
        }
    }


    protected void updateNumericAccounts(HashMap<String, Long> modifyHashMap,
                                         HashMap<String, Long> updatesHashMap){
        updatesHashMap.forEach((k, v) -> modifyHashMap.merge(k, v, Long::sum));

    }

    @Transactional
    protected void updateRepositoryInstance(I instanceUpdate) {

        String id = instanceUpdate.getId();

        if (id == null) throw new CoinnoteException("Exc: "+ this.getClass().getName() + ": ID shall not be null.");

        I instance = commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, this.getClass().toString()));

        //Merge existing instance with updates
        //Below we sum up fields checking the date expiry
        for (Field field : MobilityInstance.class.getDeclaredFields()){
            if(field.getName().startsWith("accountSpent")){
                field.setAccessible(true);
                try {
                    updateNumericAccounts(HashMap.class.cast(field.get(instance)),
                            HashMap.class.cast(field.get(instanceUpdate)));

                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        //Total spendings are simple merge together with all current adders
        instance.setTotalSpending(instance.getTotalSpending()
                + instanceUpdate.getTotalSpending());
        //update edited date
        instance.setEditedAt(instanceUpdate.getEditedAt());
        //save new instance back into repository
        commonInstanceRepository.save(instance);
        //Transfer state data to history service
        //<AutoInstance>super.historyClient.sendHistory(autoInstance.getUser().getUserName(), autoInstance);
        //historyClient.<MobilityInstance>sendHistory(mobilityInstance.getUserName(), mobilityInstance);
    }


    @Transactional
    public D saveRepositoryInstance(D dto, I instanceUpdate){
        //Generate id based on dto properties
        instanceUpdate.setId( Integer.toString(dto.hashcode()) );
        dto.setId(commonInstanceRepository.save(instanceUpdate).getId());

        return dto;
    }

    protected I getTimedInstance(String id){
        I instance = commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, MobilityInstance.class.toString()));

        ArrayList<HashMap<String, Long>> insertion = Arrays.stream(instance.getClass().getMethods())
                .filter(method -> method.getName().startsWith("getAccount"))
                .map(method -> {
                    try { return (HashMap<String, Long>) method.invoke(instance);}
                    catch (IllegalAccessException | InvocationTargetException e){
                        throw new CoinnoteException("Exc: " + this.getClass().getName() +
                                "getAsDto mapping exception.");
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        this.checkAndResetIfTimeExpired(instance, insertion);

        return instance;
    }
}
