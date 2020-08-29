package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.MobilityEntity;
import io.restassured.RestAssured;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MobilityApiTestIntegration {

    private static final String idName = "mobility-default";

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

    MobilityApiTestIntegration(){
    }

    private MobilityEntity mobilityEntity;

    private MobilityEntity defineMobilityEntity(){
        return MobilityEntity.builder()
                .accountDelta(0l)
                .partSpending(0d)
                .petrolSpending(0d)
                .serviceSpending(0d)
                .id(idName)
                .build();
    }

    public String createMobilityEntity(String hostName){
        //RestAssured.post(hostName + "/api/").body().then().statusCode(200);
        return null;
    }

    public String createMobilityEntity(String hostName, MobilityEntity mobilityEntity){
        //RestAssured.post(hostName + "/api/").body().then().statusCode(200);
        return null;
    }
}
