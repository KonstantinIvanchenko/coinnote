package com.coinnote.entryservice.mapper;

import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.dto.housing.HousingDto;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.AccountingTypes;
import com.coinnote.entryservice.entity.CommonInstance;
import com.coinnote.entryservice.entity.housing.HousingInstance;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import com.coinnote.entryservice.exception.CoinnoteException;
import org.mapstruct.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class DataMapper {

    private final static String methodNameGetAccountSpent = "getAccountSpent";
    private final static String getField = "get";
    private final static String setField = "set";
    private final static String dtoFieldNameSpending = "Spending";

    protected final static String periodWeekly = "weekly";
    protected final static String periodMonthly = "monthly";
    protected final static String periodYearly = "yearly";

    //@Mapping(target = "accountSpentService", ignore=true)
    //@Mapping(target = "accountSpentPetrol", ignore=true)
    //@Mapping(target = "accountSpentParts", ignore=true)
    //@Mapping(target = "totalSpending", ignore=true)
    public abstract MobilityInstance map(MobilityDto dto, String userName);
    public abstract HousingInstance map(HousingDto dto, String userName);

    public abstract MobilityDto map(MobilityInstance mobilityInstance, String period);
    public abstract HousingDto map(HousingInstance housingInstance, String period);

    protected <D extends CommonDto, I extends CommonInstance> void dtoToInstanceValueInjector(CommonDto commonDto,
                                                                                              I instance){
        for (Method method : instance.getClass().getDeclaredMethods()){
            if(method.getName().startsWith(methodNameGetAccountSpent)){
                method.setAccessible(true);

                //get residual name in format "Service", "Petrol" etc..
                String itemName = method.getName().substring(methodNameGetAccountSpent.length());
                try{
                    Method dtoMethod = commonDto.getClass().getMethod(getField + itemName + dtoFieldNameSpending);
                    dtoMethod.setAccessible(true);

                    Long value = (Long)dtoMethod.invoke(commonDto);

                    @SuppressWarnings("unchecked")
                    Map<String, Long> mapObject = (Map<String, Long>) method.invoke(instance);
                    mapObject.put(AccountingTypes.w.toString(), value);
                    mapObject.put(AccountingTypes.m.toString(), value);
                    mapObject.put(AccountingTypes.y.toString(), value);

                }catch (IllegalAccessException |
                        IllegalArgumentException |
                        InvocationTargetException |
                        NoSuchMethodException e){
                    throw new CoinnoteException("Exc: " + this.getClass() + " error in value injection.");
                }
            }
        }
    }

    protected <D extends CommonDto, I extends CommonInstance> void instanceToDtoValueInjector(CommonDto commonDto,
                                                                                              String period,
                                                                                              I instance){
        for (Method method : instance.getClass().getDeclaredMethods()){
            if(method.getName().startsWith(methodNameGetAccountSpent)){
                method.setAccessible(true);

                //get residual name in format "Service", "Petrol" etc..
                String itemName = method.getName().substring(methodNameGetAccountSpent.length());
                try{
                    Method dtoMethod = commonDto.getClass().getMethod(setField + itemName + dtoFieldNameSpending);
                    dtoMethod.setAccessible(true);

                    @SuppressWarnings("unchecked")
                    Map<String, Long> mapObject = (Map<String, Long>) method.invoke(instance);

                    if (period.equals(periodWeekly))
                        dtoMethod.invoke(mapObject.get(AccountingTypes.w.toString()));
                    else if (period.equals(periodMonthly))
                        dtoMethod.invoke(mapObject.get(AccountingTypes.m.toString()));
                    else if (period.equals(periodYearly))
                        dtoMethod.invoke(mapObject.get(AccountingTypes.y.toString()));

                }catch (IllegalAccessException |
                        IllegalArgumentException |
                        InvocationTargetException |
                        NoSuchMethodException e){
                    throw new CoinnoteException("Exc: " + this.getClass() + " error in value injection.");
                }
            }
        }
    }

    @AfterMapping
    protected void mapMobilityDtoUpdates(MobilityInstance mobilityInstance, String period,
                                             @MappingTarget final MobilityDto mobilityDto){
        this.instanceToDtoValueInjector(mobilityDto, period, mobilityInstance);
    }

    @AfterMapping
    protected void mapHousingDtoUpdates(HousingInstance housingInstance, String period,
                                         @MappingTarget final HousingDto housingDto){
        this.instanceToDtoValueInjector(housingDto, period, housingInstance);
    }

    @AfterMapping
    protected void mapMobilityAccountUpdates(MobilityDto dto,
                                         @MappingTarget final MobilityInstance mobilityInstance){

        this.dtoToInstanceValueInjector(dto, mobilityInstance);

        /*
        mobilityInstance.getAccountSpentService().put(AccountingTypes.w.toString(), dto.serviceSpending.longValue());
        mobilityInstance.getAccountSpentService().put(AccountingTypes.m.toString(), dto.serviceSpending.longValue());
        mobilityInstance.getAccountSpentService().put(AccountingTypes.y.toString(), dto.serviceSpending.longValue());

        mobilityInstance.getAccountSpentPetrol().put(AccountingTypes.w.toString(), dto.petrolSpending.longValue());
        mobilityInstance.getAccountSpentPetrol().put(AccountingTypes.m.toString(), dto.petrolSpending.longValue());
        mobilityInstance.getAccountSpentPetrol().put(AccountingTypes.y.toString(), dto.petrolSpending.longValue());

        mobilityInstance.getAccountSpentParts().put(AccountingTypes.w.toString(), dto.partSpending.longValue());
        mobilityInstance.getAccountSpentParts().put(AccountingTypes.m.toString(), dto.partSpending.longValue());
        mobilityInstance.getAccountSpentParts().put(AccountingTypes.y.toString(), dto.partSpending.longValue());

        mobilityInstance.getAccountSpentInsurance().put(AccountingTypes.w.toString(), dto.insuranceSpending.longValue());
        mobilityInstance.getAccountSpentInsurance().put(AccountingTypes.m.toString(), dto.insuranceSpending.longValue());
        mobilityInstance.getAccountSpentInsurance().put(AccountingTypes.y.toString(), dto.insuranceSpending.longValue());

        mobilityInstance.getAccountSpentPublic().put(AccountingTypes.w.toString(), dto.publicSpending.longValue());
        mobilityInstance.getAccountSpentPublic().put(AccountingTypes.m.toString(), dto.publicSpending.longValue());
        mobilityInstance.getAccountSpentPublic().put(AccountingTypes.y.toString(), dto.publicSpending.longValue());

        mobilityInstance.getAccountSpentOther().put(AccountingTypes.w.toString(), dto.otherSpending.longValue());
        mobilityInstance.getAccountSpentOther().put(AccountingTypes.m.toString(), dto.otherSpending.longValue());
        mobilityInstance.getAccountSpentOther().put(AccountingTypes.y.toString(), dto.otherSpending.longValue());

         */

        mobilityInstance.setTotalSpending(dto.getAccountDelta());
    }

    @AfterMapping
    protected void mapHousingAccountUpdates(HousingDto dto,
                                         @MappingTarget final HousingInstance housingInstance){

        this.dtoToInstanceValueInjector(dto, housingInstance);
        /*
        housingInstance.getAccountSpentInsurance().put(AccountingTypes.w.toString(), dto.insuranceSpending.longValue());
        housingInstance.getAccountSpentInsurance().put(AccountingTypes.m.toString(), dto.insuranceSpending.longValue());
        housingInstance.getAccountSpentInsurance().put(AccountingTypes.y.toString(), dto.insuranceSpending.longValue());

        housingInstance.getAccountSpentCleaning().put(AccountingTypes.w.toString(), dto.cleaningSpending.longValue());
        housingInstance.getAccountSpentCleaning().put(AccountingTypes.m.toString(), dto.cleaningSpending.longValue());
        housingInstance.getAccountSpentCleaning().put(AccountingTypes.y.toString(), dto.cleaningSpending.longValue());

        housingInstance.getAccountSpentMortgage().put(AccountingTypes.w.toString(), dto.mortgageSpending.longValue());
        housingInstance.getAccountSpentMortgage().put(AccountingTypes.m.toString(), dto.mortgageSpending.longValue());
        housingInstance.getAccountSpentMortgage().put(AccountingTypes.y.toString(), dto.mortgageSpending.longValue());

        housingInstance.getAccountSpentRent().put(AccountingTypes.w.toString(), dto.rentSpending.longValue());
        housingInstance.getAccountSpentRent().put(AccountingTypes.m.toString(), dto.rentSpending.longValue());
        housingInstance.getAccountSpentRent().put(AccountingTypes.y.toString(), dto.rentSpending.longValue());

        housingInstance.getAccountSpentRepair().put(AccountingTypes.w.toString(), dto.repairSpending.longValue());
        housingInstance.getAccountSpentRepair().put(AccountingTypes.m.toString(), dto.repairSpending.longValue());
        housingInstance.getAccountSpentRepair().put(AccountingTypes.y.toString(), dto.repairSpending.longValue());

        housingInstance.getAccountSpentUtility().put(AccountingTypes.w.toString(), dto.utilitySpending.longValue());
        housingInstance.getAccountSpentUtility().put(AccountingTypes.m.toString(), dto.utilitySpending.longValue());
        housingInstance.getAccountSpentUtility().put(AccountingTypes.y.toString(), dto.utilitySpending.longValue());

        housingInstance.getAccountSpentOther().put(AccountingTypes.w.toString(), dto.otherSpending.longValue());
        housingInstance.getAccountSpentOther().put(AccountingTypes.m.toString(), dto.otherSpending.longValue());
        housingInstance.getAccountSpentOther().put(AccountingTypes.y.toString(), dto.otherSpending.longValue());

         */

        housingInstance.setTotalSpending(dto.getAccountDelta());
    }
}