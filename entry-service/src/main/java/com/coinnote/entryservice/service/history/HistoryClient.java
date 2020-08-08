package com.coinnote.entryservice.service.history;

import com.coinnote.entryservice.enitity.CommonInstance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "history-service", fallback = HistoryClientFallback.class)
public interface HistoryClient{
    @RequestMapping(method = RequestMethod.PUT,
            value = "/history/{userName}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    <I extends CommonInstance>  void sendHistory(@PathVariable("userName") String userName,
                                                 @RequestBody I commonInstance);
}
