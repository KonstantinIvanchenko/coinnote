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
    @Value("${custom.keycloak.admin-id}")
    String KeycloakResource;
    @Value("${custom.keycloak.admin-secret}")
    String KeycloakCredentialsSecret;

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