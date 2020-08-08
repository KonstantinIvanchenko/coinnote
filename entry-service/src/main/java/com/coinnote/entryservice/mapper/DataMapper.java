package com.coinnote.entryservice.mapper;

import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.dto.auto.AutoDto;
import com.coinnote.entryservice.dto.auto.HouseDto;
import com.coinnote.entryservice.enitity.AccountingTypes;
import com.coinnote.entryservice.enitity.CommonInstance;
import com.coinnote.entryservice.enitity.User;
import com.coinnote.entryservice.enitity.auto.AutoInstance;
import com.coinnote.entryservice.enitity.house.HouseInstance;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashMap;

/*
@Mapper(componentModel = "spring")
public interface DataMapper<I extends CommonInstance, D extends CommonDto> {

    //Maps: id, title
    I mapDtoToInstance(D dto, User user);

    D mapInstanceToDto(I instance);


}
 */

@Mapper(componentModel = "spring")
public interface DataMapper {

    //Maps: id, title


    //TODO: check mapper instance is not autogenerated

    AutoInstance map(AutoDto dto);
    HouseInstance map(HouseDto dto);

    AutoDto map(AutoInstance autoInstance);
    HouseDto map(HouseInstance houseInstance);

    @AfterMapping
    default void mapAutoAccountUpdates(AutoDto dto,
                                       @MappingTarget final AutoInstance autoInstance){

        autoInstance.getAccountSpentService().put(AccountingTypes.w.toString(), dto.serviceSpending.longValue());
        autoInstance.getAccountSpentService().put(AccountingTypes.m.toString(), dto.serviceSpending.longValue());
        autoInstance.getAccountSpentService().put(AccountingTypes.y.toString(), dto.serviceSpending.longValue());

        autoInstance.getAccountSpentPetrol().put(AccountingTypes.w.toString(), dto.petrolSpending.longValue());
        autoInstance.getAccountSpentPetrol().put(AccountingTypes.m.toString(), dto.petrolSpending.longValue());
        autoInstance.getAccountSpentPetrol().put(AccountingTypes.y.toString(), dto.petrolSpending.longValue());

        autoInstance.getAccountSpentParts().put(AccountingTypes.w.toString(), dto.partSpending.longValue());
        autoInstance.getAccountSpentParts().put(AccountingTypes.m.toString(), dto.partSpending.longValue());
        autoInstance.getAccountSpentParts().put(AccountingTypes.y.toString(), dto.partSpending.longValue());
    }


}