package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.MobilityEntity;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Configuration
public class MobilityApiTestIntegration {

    private static final String idName = "mobility-default";

    private final static String mobility = "/mobility";
    private final static String housing = "/housing";

    private final static String create = "/create";
    private final static String update = "/update";

    private final static String mobilityCreate = mobility+create;
    private final static String mobilityUpdate = mobility+update;
    private final static String mobilityById = mobility;

    private final static String housingCreate = housing+create;
    private final static String housingUpdate = housing+update;
    private final static String housingById = housing;

    MobilityApiTestIntegration(){
    }

    @Autowired
    private MobilityEntity mobilityEntity;

    @Bean
    public MobilityEntity defineMobilityEntity(){
        return MobilityEntity.builder()
                .accountDelta(0L)
                .partSpending(0d)
                .petrolSpending(0d)
                .serviceSpending(0d)
                .title("default-test")
                .build();
    }

    public MobilityDto createMobilityEntity(String hostName){
        //RestAssured.post(hostName + "/api/").body().then().statusCode(200);

        MobilityDto response = RestAssured.given()
                .contentType("application/json")
                .body(this.mobilityEntity)
                .when()
                .post(hostName + "/api" + mobilityCreate)
                .body().as(MobilityDto.class);

        return response;
    }

    public MobilityDto createMobilityEntity(String hostName, MobilityEntity mobilityEntityCustom){
        //RestAssured.post(hostName + "/api/").body().then().statusCode(200);
        MobilityDto response = RestAssured.given()
                .contentType("application/json")
                .body(mobilityEntityCustom)
                .when()
                .post(hostName + "/api" + mobilityCreate)
                .body().as(MobilityDto.class);

        return response;
    }

    public int updateMobilityEntity(String hostName, MobilityDto mobilityDto){

        return  RestAssured.given()
                .contentType("application/json")
                .body(mobilityDto)
                .when()
                .post(hostName + "/api" + mobilityUpdate)
                .getStatusCode();
    }

    public MobilityDto getMobilityEntityById(String hostName, String id){
        MobilityDto response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get(hostName + "/api" + mobilityById + "/" + id)
                .body().as(MobilityDto.class);

        return response;
    }
}
