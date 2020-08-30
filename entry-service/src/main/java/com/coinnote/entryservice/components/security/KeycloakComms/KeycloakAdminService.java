package com.coinnote.entryservice.components.security.KeycloakComms;


import com.coinnote.entryservice.components.security.rolemgmt.KeycloakServicesRole;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

@AllArgsConstructor
@Component
@Profile("!test")
public class KeycloakAdminService {

    @Autowired
    private final KeycloakAccessSettings keycloakAccessSettings;
    @Autowired
    private final Keycloak keycloakAdmin;
    @Autowired
    private final KeycloakServicesRole keycloakServicesRole;

    /**
     * Create user within keycloak realm and assign roles.
     * @param userRepresentation
     */
    public void saveUserKeycloak(UserRepresentation userRepresentation){
        RealmResource realmResource = keycloakAdmin.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm);
        //realmResource.users().create(userRepresentation);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(userRepresentation);
        String userId = CreatedResponseUtil.getCreatedId(response);

        //enable access to other microservices while current user logged in
        keycloakServicesRole.setServicesRoles(realmResource, usersResource, userId);
    }

    /**
     * Get users with the name from the keycloak realm.
     * @param userName
     * @return
     */
    public List<UserRepresentation> getUsersByUsername(String userName){

        RealmResource realmResource = keycloakAdmin.realm(keycloakAccessSettings.getKeycloakAdminClientConfig().KeycloakRealm);
        List<UserRepresentation> users = realmResource.users().search(userName);

        return users;
    }

    /**
     * After successful user registry in Keycloak login, it will redirect user session to a Custom page
     * After that SecurityContext shall contain current user session. Which would allow to get bearer token.
     * @return
     */
    public String getUserToken(){
        KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();

        @SuppressWarnings(value = "unchecked")
        KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication
                .getPrincipal();


        return keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
    }

    /**
     * Get userid for principal security context holder.
     * @return
     */
    public String getPrincipalUserId(){
        return this.getUsersByUsername(this.getPrincipal()
                .getKeycloakSecurityContext()
                .getIdToken()
                .getPreferredUsername())
                .get(0).getId();
    }

    /**
     * Get userName for principal security context holder.
     * @return
     */
    public String getPrincipalUserName(){
        return this.getPrincipal().getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    /**
     * Extract information about principal security context holder.
     * @return
     */
    private KeycloakPrincipal getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
            throw new CoinnoteException("Principal data check while unauthenticated..");
        else{
            Object principal = authentication.getPrincipal();

            if (principal instanceof KeycloakPrincipal)
                return KeycloakPrincipal.class.cast(principal);
            else throw new CoinnoteException("Principal authentication is not provided by Keycloak..");
        }
    }
}
