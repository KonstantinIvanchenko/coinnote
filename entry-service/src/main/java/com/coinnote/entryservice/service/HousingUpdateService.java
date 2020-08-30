package com.coinnote.entryservice.service;

import com.coinnote.entryservice.dto.housing.HousingDto;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.housing.HousingInstance;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
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
        HousingInstance housingInstanceUpdate = this.mapDtoToInstance(dto.setTotalSpending());

        super.updateRepositoryInstance(housingInstanceUpdate);
    }

    private HousingInstance mapDtoToInstance(HousingDto dto){
        return super.dataMapper.map(dto, super.keycloakAdminService.getPrincipalUserName());
    }

    private HousingDto mapInstanceToDto(HousingInstance housingInstance, String period){
        return super.dataMapper.map(housingInstance, period);
    }

    @Override
    @Transactional
    public HousingDto saveAsInstance(HousingDto dto){
        //Ensure all fields are reset on object save
        dto.setAllZero();
        dto.setAccountDeltaZero();
        HousingInstance instanceUpdate = this.mapDtoToInstance(dto);
        return super.saveRepositoryInstance(dto, instanceUpdate);
    }

    @Override
    @Transactional
    public HousingDto getAsDto(String id, String period){

        if (period == null)
            return null;

        HousingInstance housingInstance = super.getTimedInstance(id);

        return this.mapInstanceToDto(housingInstance, period);
    }

    @Override
    @Transactional
    public HousingDto getAsDto(Long id, String period){
        return getAsDto(id.toString(), period);
    }
}
