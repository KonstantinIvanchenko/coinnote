package com.coinnote.entryservice.service.auth;

import com.coinnote.entryservice.enitity.CommonInstance;

import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "auth-service")
public interface AuthClient {

    @RequestMapping(method = RequestMethod.POST,
            value = "/coinnote/users")
    HttpFacade.Response createUser(@RequestBody UserRepresentation userRepresentation);


    @RequestMapping(method = RequestMethod.GET,
            value = "/coinnote/users")
    List<UserRepresentation> getUserByUsername(@RequestParam String username, @RequestHeader("Authorization") String bearerToken);

}
