package com.coinnote.historyservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/histupd")
public class HistoryController {

    @RequestMapping("/1")
    //@PreAuthorize("hasAnyAuthority('ROLE_WRITE_HIST')")
    public String updateHistory(){
        return "History updated !!!";
    }
}
