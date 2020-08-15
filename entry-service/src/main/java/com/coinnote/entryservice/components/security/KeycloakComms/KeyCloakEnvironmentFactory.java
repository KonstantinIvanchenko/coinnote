package com.coinnote.entryservice.components.security.KeycloakComms;

import org.springframework.stereotype.Component;

@Component
public class KeyCloakEnvironmentFactory {
    public KeycloakEnvironmentValues newKeyCloakEnvironmentValues(){
        KeycloakEnvironmentValues environmentValues = new KeycloakEnvironmentValues();
        //environmentValues.setKeycloakAdminClientConfig(environmentValues.loadKeycloakAdminClientConfig());
        return environmentValues;
    }
}
