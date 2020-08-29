package com.coinnote.entryservice.service;

import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class MobilityUpdateService extends DataUpdateService<MobilityInstance, MobilityDto> {

    @Override
    @Transactional
    public void updateAsInstance(MobilityDto dto) {
        MobilityInstance mobilityInstanceUpdate = this.mapDtoToInstance(dto.setTotalSpending());

        String id = mobilityInstanceUpdate.getId();

        if (id == null) throw new CoinnoteException("Exc: "+ this.getClass().getName() + ": ID shall not be null.");

        MobilityInstance mobilityInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, MobilityInstance.class.toString()));

        //Merge existing instance with updates
        //Below we sum up fields checking the date expiry
        for (Field field : MobilityInstance.class.getDeclaredFields()){
            if(field.getGenericType() instanceof HashMap){
                field.setAccessible(true);
                try {
                    updateNumericAccounts(HashMap.class.cast(field.get(mobilityInstance)),
                                        HashMap.class.cast(field.get(mobilityInstanceUpdate)));

                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        //Total spendings are simple merge together with all current adders
        mobilityInstance.setTotalSpending(mobilityInstance.getTotalSpending()
                + mobilityInstanceUpdate.getTotalSpending());
        //update edited date
        mobilityInstance.setEditedAt(mobilityInstanceUpdate.getEditedAt());
        //save new instance back into repository
        super.commonInstanceRepository.save(mobilityInstance);
        //Transfer state data to history service
        //<AutoInstance>super.historyClient.sendHistory(autoInstance.getUser().getUserName(), autoInstance);
        super.historyClient.<MobilityInstance>sendHistory(mobilityInstance.getUserName(), mobilityInstance);
    }

    private MobilityInstance mapDtoToInstance(MobilityDto dto){
        return super.dataMapper.map(dto, super.keycloakAdminService.getPrincipalUserName());
    }

    private MobilityDto mapInstanceToDto(MobilityInstance mobilityInstance){
        return super.dataMapper.map(mobilityInstance);
    }

    @Override
    @Transactional
    public MobilityDto saveAsInstance(MobilityDto dto){
        //Ensure all fields are reset on object save
        dto.setAllZero();

        MobilityInstance mobilityInstanceUpdate = this.mapDtoToInstance(dto);
        //Generate id based on dto properties
        mobilityInstanceUpdate.setId( Integer.toString(dto.hashcode()) );

        MobilityInstance save = super.commonInstanceRepository.save(mobilityInstanceUpdate);
        dto.setId(save.getId());

        return dto;
    }

    @Override
    @Transactional
    public MobilityDto getAsDto(String id){
        MobilityInstance mobilityInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, MobilityInstance.class.toString()));

        ArrayList<HashMap<String, Long>> insertion = Arrays.stream(mobilityInstance.getClass().getMethods())
                    .filter(method -> method.getName().startsWith("getAccount"))
                    .map(method -> {
                        try { return (HashMap<String, Long>) method.invoke(mobilityInstance);}
                        catch (IllegalAccessException | InvocationTargetException e){
                            throw new CoinnoteException("Exc: " + this.getClass().getName() +
                                    "getAsDto mapping exception.");
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));


        super.checkAndResetIfTimeExpired(mobilityInstance, insertion);

        return this.mapInstanceToDto(mobilityInstance);
    }

    @Override
    @Transactional
    public MobilityDto getAsDto(Long id){
        MobilityInstance mobilityInstance = super.commonInstanceRepository
                .findById(id.toString())
                .orElseThrow(() -> new CoinnoteException(id.toString(), MobilityInstance.class.toString()));

        ArrayList<HashMap<String, Long>> insertion = Arrays.stream(mobilityInstance.getClass().getMethods())
                .filter(method -> method.getName().startsWith("getAccount"))
                .map(method -> {
                    try { return (HashMap<String, Long>) method.invoke(mobilityInstance);}
                    catch (IllegalAccessException | InvocationTargetException e){
                        throw new CoinnoteException("Exc: "+ this.getClass().getName() +
                                "getAsDto mapping exception.");
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));


        super.checkAndResetIfTimeExpired(mobilityInstance, insertion);

        return this.mapInstanceToDto(mobilityInstance);
    }
}
