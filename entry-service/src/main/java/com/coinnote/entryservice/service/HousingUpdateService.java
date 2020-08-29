package com.coinnote.entryservice.service;

import com.coinnote.entryservice.dto.housing.HousingDto;
import com.coinnote.entryservice.entity.housing.HousingInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class HousingUpdateService extends DataUpdateService<HousingInstance, HousingDto> {

    @Override
    @Transactional
    public void updateAsInstance(HousingDto dto) {
        HousingInstance HousingInstanceUpdate = this.mapDtoToInstance(dto.setTotalSpending());

        String id = HousingInstanceUpdate.getId();

        if (id == null) throw new CoinnoteException("ID shall not be null.");

        HousingInstance HousingInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, HousingInstance.class.toString()));

        //Merge existing instance with updates
        //Below we sum up fields checking the date expiry
        for (Field field : HousingInstance.class.getDeclaredFields()){
            if(field.getGenericType() instanceof HashMap){
                field.setAccessible(true);
                try {
                    updateNumericAccounts(HashMap.class.cast(field.get(HousingInstance)),
                                        HashMap.class.cast(field.get(HousingInstanceUpdate)));

                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        //Total spendings are simple merge together with all current adders
        HousingInstance.setTotalSpending(HousingInstance.getTotalSpending()
                + HousingInstanceUpdate.getTotalSpending());
        //update edited date
        HousingInstance.setEditedAt(HousingInstanceUpdate.getEditedAt());
        //save new instance back into repository
        super.commonInstanceRepository.save(HousingInstance);
        //Transfer state data to history service
        //<AutoInstance>super.historyClient.sendHistory(autoInstance.getUser().getUserName(), autoInstance);
        super.historyClient.<HousingInstance>sendHistory(HousingInstance.getUserName(), HousingInstance);
    }

    private HousingInstance mapDtoToInstance(HousingDto dto){
        return super.dataMapper.map(dto, super.keycloakAdminService.getPrincipalUserName());
    }

    private HousingDto mapInstanceToDto(HousingInstance HousingInstance){
        return super.dataMapper.map(HousingInstance);
    }

    @Override
    @Transactional
    public HousingDto saveAsInstance(HousingDto dto){
        //Ensure all fields are reset on object save
        dto.setAllZero();

        HousingInstance HousingInstanceUpdate = this.mapDtoToInstance(dto);
        //Generate id based on dto properties
        HousingInstanceUpdate.setId( Integer.toString(dto.hashcode()) );

        HousingInstance save = super.commonInstanceRepository.save(HousingInstanceUpdate);
        dto.setId(save.getId());

        return dto;
    }

    @Override
    @Transactional
    public HousingDto getAsDto(String id){
        HousingInstance HousingInstance = super.commonInstanceRepository
                .findById(id)
                .orElseThrow(() -> new CoinnoteException(id, HousingInstance.class.toString()));

        ArrayList<HashMap<String, Long>> insertion = Arrays.stream(HousingInstance.getClass().getMethods())
                    .filter(method -> method.getName().startsWith("getAccount"))
                    .map(method -> {
                        try { return (HashMap<String, Long>) method.invoke(HousingInstance);}
                        catch (IllegalAccessException | InvocationTargetException e){
                            throw new CoinnoteException("Exc: getAsDto mapping exception.");
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));


        super.checkAndResetIfTimeExpired(HousingInstance, insertion);

        return this.mapInstanceToDto(HousingInstance);
    }

    @Override
    @Transactional
    public HousingDto getAsDto(Long id){
        HousingInstance HousingInstance = super.commonInstanceRepository
                .findById(id.toString())
                .orElseThrow(() -> new CoinnoteException(id.toString(), HousingInstance.class.toString()));

        ArrayList<HashMap<String, Long>> insertion = Arrays.stream(HousingInstance.getClass().getMethods())
                .filter(method -> method.getName().startsWith("getAccount"))
                .map(method -> {
                    try { return (HashMap<String, Long>) method.invoke(HousingInstance);}
                    catch (IllegalAccessException | InvocationTargetException e){
                        throw new CoinnoteException("Exc: getAsDto mapping exception.");
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));


        super.checkAndResetIfTimeExpired(HousingInstance, insertion);

        return this.mapInstanceToDto(HousingInstance);
    }
}
