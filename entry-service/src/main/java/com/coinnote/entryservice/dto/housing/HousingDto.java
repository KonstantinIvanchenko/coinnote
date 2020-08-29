package com.coinnote.entryservice.dto.housing;

import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.util.AccountSpentHashMap;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class HousingDto extends CommonDto {

    public Double mortgageSpending;
    public Double utilitySpending;
    public Double cleaningSpending;
    public Double rentSpending;
    public Double repairSpending;
    public Double insuranceSpending;
    public Double otherSpending;

    public void setAllZero(){
        super.setAccountDeltaZero();
        this.mortgageSpending = 0d;
        this.utilitySpending = 0d;
        this.cleaningSpending = 0d;
        this.rentSpending = 0d;
        this.rentSpending = 0d;
        this.repairSpending = 0d;
        this.insuranceSpending = 0d;
        this.otherSpending = 0d;
    }

    public HousingDto setTotalSpending(){
        super.setAccountDelta(Double.doubleToLongBits(this.mortgageSpending+
                                                        this.utilitySpending+
                                                        this.cleaningSpending+
                                                        this.rentSpending+
                                                        this.rentSpending+
                                                        this.repairSpending+
                                                        this.insuranceSpending+
                                                        this.otherSpending));
        return this;
    }
}
