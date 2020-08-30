package com.coinnote.entryservice.dto.mobility;

import com.coinnote.entryservice.dto.CommonDto;
import com.coinnote.entryservice.entity.util.AccountSpentHashMap;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MobilityDto extends CommonDto {
    public Double partSpending;
    public Double serviceSpending;
    public Double petrolSpending;
    public Double insuranceSpending;
    public Double publicSpending;
    public Double otherSpending;

    public void setAllZero(){
        super.setAccountDeltaZero();
        this.partSpending=0d;
        this.serviceSpending=0d;
        this.petrolSpending=0d;
        this.insuranceSpending=0d;
        this.publicSpending=0d;
        this.otherSpending=0d;
    }

    public MobilityDto setTotalSpending(){
        super.setAccountDelta((long)(this.partSpending+
                this.serviceSpending+
                this.petrolSpending+
                this.insuranceSpending+
                this.publicSpending+
                this.otherSpending));

        return this;
    }
}
