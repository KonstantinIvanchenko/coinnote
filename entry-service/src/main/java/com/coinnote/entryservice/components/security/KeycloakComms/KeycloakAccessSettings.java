package com.coinnote.entryservice.components.security.KeycloakComms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class KeycloakAccessSettings {


    @Autowired
    private KeycloakAdminClientConfig keycloakAdminClientConfig;

    private void setKeycloakAdminClientConfig(KeycloakAdminClientConfig config){
        this.keycloakAdminClientConfig = config;
    }

    public KeycloakAdminClientConfig getKeycloakAdminClientConfig(){
        return keycloakAdminClientConfig;
    }
}
