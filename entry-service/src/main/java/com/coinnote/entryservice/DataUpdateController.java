package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.housing.HousingDto;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.housing.HousingInstance;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import com.coinnote.entryservice.service.DataUpdateService;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DataUpdateController {

    private final static  String id = "/{id}";

    private final static String mobility = "/mobility";
    private final static String housing = "/housing";

    private final static String create = "/create";
    private final static String update = "/update";

    private final static String mobilityCreate = mobility+create;
    private final static String mobilityUpdate = mobility+update;
    private final static String mobilityById = mobility+id;

    private final static String housingCreate = housing+create;
    private final static String housingUpdate = housing+update;
    private final static String housingById = housing+id;

    private final DataUpdateService<MobilityInstance, MobilityDto> dataUpdateServiceMobility;
    private final DataUpdateService<HousingInstance, HousingDto> dataUpdateServiceHousing;

    //-----------------------------------------------------------------------------------------------------------------
    @PostMapping(mobilityCreate)
    public ResponseEntity<MobilityDto> createMobilityInstance(@RequestBody MobilityDto mobilityDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceMobility.saveAsInstance(mobilityDto));
    }

    @PostMapping(mobilityUpdate)
    public ResponseEntity<Void> updateMobilityInstance(@RequestBody MobilityDto mobilityDto){
        dataUpdateServiceMobility.updateAsInstance(mobilityDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(mobilityById)
    public ResponseEntity<MobilityDto> getMobilityInstance(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceMobility.getAsDto(id));
    }
    //-----------------------------------------------------------------------------------------------------------------
    @PostMapping(housingCreate)
    public ResponseEntity<HousingDto> createHousingInstance(@RequestBody HousingDto housingDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceHousing.saveAsInstance(housingDto));
    }

    @PostMapping(housingUpdate)
    public ResponseEntity<Void> updateHousingInstance(@RequestBody HousingDto housingDto){
        dataUpdateServiceHousing.updateAsInstance(housingDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(housingById)
    public ResponseEntity<HousingDto> getHousingInstance(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.CREATED).body(dataUpdateServiceHousing.getAsDto(id));
    }
    //-----------------------------------------------------------------------------------------------------------------


    @Autowired
    KeycloakRestTemplate keycloakRestTemplate;

    // Debug only
    @PostMapping("/passhist")
    public ResponseEntity<Void> TESTpassToHistoryService(){
        String temp = keycloakRestTemplate.getForEntity("http://localhost:8001/api/histupd/1", String.class).getBody();
        System.out.println("!!!! TEMP DEBUG:: "+ temp);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // Debug only
    @GetMapping("/")
    public ResponseEntity<String> TESTreturnString(){
        return ResponseEntity.status(HttpStatus.OK).body(new String("Access to /api OK"));
    }
}
