package com.coinnote.entryservice.components.security.KeycloakComms;

import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:application.properties")
@Component
public class KeycloakEnvironmentValues {

    @Autowired
    private KeycloakAdminClientConfig keycloakAdminClientConfig;

    public KeycloakAdminClientConfig getKeycloakAdminClientConfig(){
        return keycloakAdminClientConfig;
    }

    public void setKeycloakAdminClientConfig(KeycloakAdminClientConfig keycloakAdminClientConfig){
        this.keycloakAdminClientConfig = keycloakAdminClientConfig;
    }
/*
    //Use if Environmental autowiring works correctly
    public KeycloakAdminClientConfig loadKeycloakAdminClientConfig(){

        try{
            if (!StringUtils.isBlank(this.KeycloakAuthServerUrl))
                throw new CoinnoteException("AuthServer variable empty");
            keycloakAdminClientConfig.setServerUrl(this.KeycloakAuthServerUrl);

            if (!StringUtils.isBlank(this.KeycloakRealm))
                throw new CoinnoteException("Realm variable empty");
            keycloakAdminClientConfig.setRealm(this.KeycloakRealm);

            if (!StringUtils.isBlank(this.KeycloakResource))
                throw new CoinnoteException("Resource variable empty");
            keycloakAdminClientConfig.setClientId(this.KeycloakResource);

            if (!StringUtils.isBlank(this.KeycloakCredentialsSecret))
                throw new CoinnoteException("CredentialSecret variable empty");
            keycloakAdminClientConfig.setClientSecret(this.KeycloakCredentialsSecret);

        }catch (CoinnoteException e){
            e.printStackTrace();
        }

        return keycloakAdminClientConfig;
    }
    */
}
