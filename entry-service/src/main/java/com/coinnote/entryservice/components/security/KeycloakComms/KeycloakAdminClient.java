package com.coinnote.entryservice.components.security.KeycloakComms;

import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
@AllArgsConstructor
public class KeycloakAdminClient {
    @Autowired
    private final KeycloakAdminClientConfig keycloakAdminClientConfig;

    @Bean
    public Keycloak keycloakAdminClientCreate() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080/auth/")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                //.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm)
                .realm(this.keycloakAdminClientConfig.getKeycloakRealm())
                .clientId(this.keycloakAdminClientConfig.getKeycloakResource())
                .clientSecret(this.keycloakAdminClientConfig.getKeycloakCredentialsSecret())
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();

        return keycloak;
    }
}
