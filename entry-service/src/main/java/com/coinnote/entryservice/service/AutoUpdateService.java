package com.coinnote.entryservice.service;

import com.coinnote.entryservice.dto.auto.AutoDto;
import com.coinnote.entryservice.enitity.AccountingTypes;
import com.coinnote.entryservice.enitity.User;
import com.coinnote.entryservice.enitity.auto.AutoInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AutoUpdateService extends DataUpdateService<AutoInstance, AutoDto> {

    @Override
    @Transactional
    public void updateAsInstance(AutoDto dto) {
        AutoInstance autoInstanceUpdate = this.mapDtoToInstance(dto.setTotalSpending());

        //Long id = autoInstanceUpdate.getId();
        String id = autoInstanceUpdate.getId();

        if (id == null) throw new CoinnoteException("ID shall not be null.");

        AutoInstance autoInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, AutoInstance.class.toString()));

        //Merge existing instance with updates
        //Below we sum up fields checking the date expiry
        for (Field field : AutoInstance.class.getDeclaredFields()){
            if(field.getGenericType() instanceof HashMap){
                field.setAccessible(true);
                try {
                    updateNumericAccounts(HashMap.class.cast(field.get(autoInstance)),
                                        HashMap.class.cast(field.get(autoInstanceUpdate)));

                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        //Total spendings are simple merge together with all current adders
        autoInstance.setTotalSpending(autoInstance.getTotalSpending() + autoInstanceUpdate.getTotalSpending());
        //update edited date
        autoInstance.setEditedAt(autoInstanceUpdate.getEditedAt());
        //save new instance back into repository
        super.commonInstanceRepository.save(autoInstance);
        //Transfer state data to history service
        //<AutoInstance>super.historyClient.sendHistory(autoInstance.getUser().getUserName(), autoInstance);
        super.historyClient.<AutoInstance>sendHistory(autoInstance.getUser().getUserName(), autoInstance);
    }

    private AutoInstance mapDtoToInstance(AutoDto dto){
        //TODO: enable USER
        User user = new User();
        user.setId("0");
        user.setUserName("first_test");
        return super.dataMapper.map(dto);
    }

    private AutoDto mapInstanceToDto(AutoInstance autoInstance){
        //TODO: enable USER
        User user = new User();
        return super.dataMapper.map(autoInstance);
    }

    @Override
    @Transactional
    public AutoDto saveAsInstance(AutoDto dto){

        //Ensure all fields are reset on object save
        dto.setAllZero();

        AutoInstance autoInstanceUpdate = this.mapDtoToInstance(dto);
        //Generate id based on dto properties
        autoInstanceUpdate.setId( Integer.toString(dto.hashcode()) );

        AutoInstance save = super.commonInstanceRepository.save(autoInstanceUpdate);
        dto.setId(save.getId());
        dto.setId(save.getId());

        return dto;
    }

    @Override
    @Transactional
    public AutoDto getAsDto(String id){
        AutoInstance autoInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, AutoInstance.class.toString()));

        super.checkAndResetIfTimeExpired(autoInstance, new ArrayList<>(){
            {
                add((HashMap<String, Long>) autoInstance.getAccountSpentService());
                add((HashMap<String, Long>) autoInstance.getAccountSpentParts());
                add((HashMap<String, Long>) autoInstance.getAccountSpentPetrol());
            }
        });

        return this.mapInstanceToDto(autoInstance);
    }

    @Override
    @Transactional
    public AutoDto getAsDto(Long id){
        AutoInstance autoInstance = super.commonInstanceRepository
                .findById(id.toString())
                .orElseThrow(() -> new CoinnoteException(id.toString(), AutoInstance.class.toString()));

        super.checkAndResetIfTimeExpired(autoInstance, new ArrayList<>(){
            {
                add((HashMap<String, Long>) autoInstance.getAccountSpentService());
                add((HashMap<String, Long>) autoInstance.getAccountSpentParts());
                add((HashMap<String, Long>) autoInstance.getAccountSpentPetrol());
            }
        });

        return this.mapInstanceToDto(autoInstance);
    }
}
