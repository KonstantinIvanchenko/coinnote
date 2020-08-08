package com.coinnote.entryservice.enitity.auto;

import com.coinnote.entryservice.enitity.AccountingTypes;
import com.coinnote.entryservice.enitity.CommonInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AutoInstance extends CommonInstance {

    //@Singular("accountSpentTot")
    Map<String, Long> accountSpentService = new HashMap<>(){
        {
            put(AccountingTypes.y.toString(), 0L);
            put(AccountingTypes.m.toString(), 0L);
            put(AccountingTypes.w.toString(), 0L);
        }
    };

    //@Singular("accountSpentParts")
    Map<String, Long> accountSpentParts = new HashMap<>(){
        {
            put(AccountingTypes.y.toString(), 0L);
            put(AccountingTypes.m.toString(), 0L);
            put(AccountingTypes.w.toString(), 0L);
        }
    };

    Map<String, Long> accountSpentPetrol = new HashMap<>(){
        {
            put(AccountingTypes.y.toString(), 0L);
            put(AccountingTypes.m.toString(), 0L);
            put(AccountingTypes.w.toString(), 0L);
        }
    };

    /*
    public void setAllZero(){
        HashMap<String, Long> zeros = new HashMap<>(){
            {
                put(AccountingTypes.y.toString(), 0L);
                put(AccountingTypes.m.toString(), 0L);
                put(AccountingTypes.w.toString(), 0L);
            }
        };
        accountSpentParts.putAll(zeros);
        accountSpentService.putAll(zeros);
        accountSpentPetrol.putAll(zeros);
    }
     */
}
