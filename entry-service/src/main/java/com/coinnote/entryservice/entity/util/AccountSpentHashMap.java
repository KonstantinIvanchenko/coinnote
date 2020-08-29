package com.coinnote.entryservice.entity.util;

import com.coinnote.entryservice.entity.AccountingTypes;

import java.util.HashMap;

/**
 *Util class for initializing YMW ___Instances
 */
public class AccountSpentHashMap extends HashMap<String, Long> {
    public AccountSpentHashMap(){
        put(AccountingTypes.y.toString(), 0L);
        put(AccountingTypes.m.toString(), 0L);
        put(AccountingTypes.w.toString(), 0L);
    }
}
