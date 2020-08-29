package com.coinnote.entryservice.service;

import com.coinnote.entryservice.components.security.KeycloakComms.KeycloakAdminService;
import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.entity.AccountingTypes;
import com.coinnote.entryservice.entity.CommonInstance;
import com.coinnote.entryservice.mapper.DataMapper;
import com.coinnote.entryservice.repository.CommonInstanceMongoRepository;
import com.coinnote.entryservice.service.history.HistoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;

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
    public abstract D getAsDto(Long id);
    @Transactional
    public abstract D getAsDto(String id);


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
}
