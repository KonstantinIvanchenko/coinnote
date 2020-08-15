package com.coinnote.entryservice.components.security.KeycloakComms;

import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Component
public class KeycloakAdminService {

    @Autowired
    private final KeycloakAccessSettings keycloakAccessSettings;
    @Autowired
    private final Keycloak keycloakAdmin;

    //TODO: here we need to add current user property provider - roles, name, etc..

    public void saveUserKeycloak(UserRepresentation userRepresentation){
        RealmResource realmResource = keycloakAdmin.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm);
        realmResource.users().create(userRepresentation);
    }

    //Useless
    /*
    public List<UserRepresentation> getUsersByUsernameTemp(String userName){

        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principalKeycloak = null;
        KeycloakPrincipal principalKeycloakRaw = null;
        Keycloak keycloak = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof KeycloakPrincipal) {
                principalKeycloak = KeycloakPrincipal.class.cast(principal);
            }
            else {
                principalKeycloakRaw = (KeycloakPrincipal) principal;
            }
        }

        if (principalKeycloakRaw != null)
            keycloak = keycloakAccessSettings.getKeycloakClient(principalKeycloakRaw.getKeycloakSecurityContext());

        return keycloak.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().getKeycloakResource())
                .users()
                .search(userName);
    }
     */


    public List<UserRepresentation> getUsersByUsernameAsAdminCli(String userName){

        //This works but not completely right to use admin console credentials to master realm

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakAuthServerUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username("admin")
                .password("ADMIN PASSWORD HERE")
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                ).build();

        RealmResource realmResource = keycloak.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm);
        List<UserRepresentation> users = realmResource.users().search(userName);

        return users;
    }


    public List<UserRepresentation> getUsersByUsername(String userName){

        RealmResource realmResource = keycloakAdmin.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm);
        List<UserRepresentation> users = realmResource.users().search(userName);

        return users;
    }
}
