package com.coinnote.entryservice.entity.mobility;

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
public class MobilityInstance extends CommonInstance {
    Map<String, Long> accountSpentService = new AccountSpentHashMap();
    Map<String, Long> accountSpentParts = new AccountSpentHashMap();
    Map<String, Long> accountSpentPetrol = new AccountSpentHashMap();
    Map<String, Long> accountSpentInsurance = new AccountSpentHashMap();
    Map<String, Long> accountSpentPublic = new AccountSpentHashMap();
    Map<String, Long> accountSpentOther = new AccountSpentHashMap();
}
