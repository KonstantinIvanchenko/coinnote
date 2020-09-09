package com.coinnote.entryservice.aspectconfig;

import com.coinnote.entryservice.entity.CommonInstance;
import com.coinnote.entryservice.service.history.HistoryClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class HistoryServiceAspect {

    @Autowired
    protected HistoryClient historyClient;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(value = "execution(com.coinnote.entryservice.entity.CommonInstance+ " +
            "com.coinnote.entryservice.service.DataUpdateService.updateRepositoryInstance(..))",
    returning = "instance")
    public void afterRunning(JoinPoint joinPoint, CommonInstance instance){
        logger.info("after execution of {}", joinPoint);

        //Due to incapability of using the generic type directly as passed object to the advice,
        //here name of instantiated object of CommonInstance base class is derived
        historyClient.sendHistory(
                instance.getUserName(),
                instance.getClass().getSimpleName(),
                instance);
    }
}