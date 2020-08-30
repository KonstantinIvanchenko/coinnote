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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

        super.updateRepositoryInstance(mobilityInstanceUpdate);
    }

    private MobilityInstance mapDtoToInstance(MobilityDto dto){
        return super.dataMapper.map(dto, super.keycloakAdminService.getPrincipalUserName());
    }

    private MobilityDto mapInstanceToDto(MobilityInstance mobilityInstance, String period){
        return super.dataMapper.map(mobilityInstance, period);
    }

    @Override
    @Transactional
    public MobilityDto saveAsInstance(MobilityDto dto){
        //Ensure all fields are reset on object save
        dto.setAllZero();
        dto.setAccountDeltaZero();
        MobilityInstance instanceUpdate = this.mapDtoToInstance(dto);
        return super.saveRepositoryInstance(dto, instanceUpdate);
    }

    @Override
    @Transactional
    public MobilityDto getAsDto(String id, String period){

        if (period == null)
            return null;

        MobilityInstance mobilityInstance = super.getTimedInstance(id);

        return this.mapInstanceToDto(mobilityInstance, period);
    }

    @Override
    @Transactional
    public MobilityDto getAsDto(Long id, String period){
        return getAsDto(id.toString(), period);
    }

}
