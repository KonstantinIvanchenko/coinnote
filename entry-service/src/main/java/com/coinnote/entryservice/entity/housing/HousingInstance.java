package com.coinnote.entryservice.entity.housing;

import com.coinnote.entryservice.entity.CommonInstance;
import com.coinnote.entryservice.entity.util.AccountSpentHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HousingInstance extends CommonInstance {
    Map<String, Long> accountSpentMortgage = new AccountSpentHashMap();
    Map<String, Long> accountSpentUtility = new AccountSpentHashMap();
    Map<String, Long> accountSpentCleaning = new AccountSpentHashMap();
    Map<String, Long> accountSpentRent = new AccountSpentHashMap();
    Map<String, Long> accountSpentRepair = new AccountSpentHashMap();
    Map<String, Long> accountSpentInsurance = new AccountSpentHashMap();
    Map<String, Long> accountSpentOther = new AccountSpentHashMap();
}
