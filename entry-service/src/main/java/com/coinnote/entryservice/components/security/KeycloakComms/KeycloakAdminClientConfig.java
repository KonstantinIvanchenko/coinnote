package com.coinnote.entryservice.components.security.KeycloakComms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


@Service
@PropertySource("classpath:application.properties")
public class KeycloakAdminClientConfig {
    @Value("${keycloak.auth-server-url}")
    String KeycloakAuthServerUrl;
    @Value("${keycloak.realm}")
    String KeycloakRealm;
    /*
    @Value("${keycloak.admin-id}")
    String KeycloakResource;
    @Value("${keycloak.admin-secret}")
    String KeycloakCredentialsSecret;

     */
    private final static String KeycloakResource = "keycloak-admin";
    private final static String KeycloakCredentialsSecret = "af95c8c9-5c06-4072-9688-e95cdef33f34";
    //keycloak.admin-id=keycloak-admin
    //keycloak.admin-secret=af95c8c9-5c06-4072-9688-e95cdef33f34

    public String getKeycloakAuthServerUrl() {
        return KeycloakAuthServerUrl;
    }

    public String getKeycloakRealm() {
        return KeycloakRealm;
    }


    public String getKeycloakResource() {
        return KeycloakResource;
    }

    public String getKeycloakCredentialsSecret() {
        return KeycloakCredentialsSecret;
    }

}