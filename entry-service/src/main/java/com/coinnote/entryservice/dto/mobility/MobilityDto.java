package com.coinnote.entryservice.dto.mobility;

import com.coinnote.entryservice.dto.CommonDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobilityDto extends CommonDto {
    public Double partSpending;
    public Double serviceSpending;
    public Double petrolSpending;

    public void setAllZero(){
        super.setAccountDeltaZero();
        this.partSpending=0d;
        this.serviceSpending=0d;
        this.petrolSpending=0d;
    }

    public MobilityDto setTotalSpending(){
        super.setAccountDelta(Double.doubleToLongBits(this.partSpending+
                                                            this.serviceSpending+
                                                            this.petrolSpending));
        return this;
    }
}
