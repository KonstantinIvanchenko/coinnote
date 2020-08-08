package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.auto.AutoDto;
import com.coinnote.entryservice.dto.auto.HouseDto;
import com.coinnote.entryservice.enitity.auto.AutoInstance;
import com.coinnote.entryservice.enitity.house.HouseInstance;
import com.coinnote.entryservice.service.AutoUpdateService;
import com.coinnote.entryservice.service.DataUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DataUpdateController {

    private final static  String id = "/{id}";

    private final static String auto = "/auto";
    private final static String house = "/house";

    private final static String create = "/create";
    private final static String update = "/update";

    private final static String autoCreate = auto+create;
    private final static String autoUpdate = auto+update;
    private final static String autoById = auto+id;

    private final DataUpdateService<AutoInstance, AutoDto> dataUpdateServiceAuto;
    //private final DataUpdateService<HouseInstance, HouseDto> dataUpdateServiceHouse;
    //private final AutoUpdateService dataUpdateServiceAuto;


    @PostMapping(autoCreate)
    public ResponseEntity<AutoDto> createAutoInstance(@RequestBody AutoDto autoDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceAuto.saveAsInstance(autoDto));
    }

    @PostMapping(autoUpdate)
    public ResponseEntity<Void> updateAutoInstance(@RequestBody AutoDto autoDto){
        dataUpdateServiceAuto.updateAsInstance(autoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(autoById)
    public ResponseEntity<AutoDto> getAutoInstance(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceAuto.getAsDto(id));
    }

}
