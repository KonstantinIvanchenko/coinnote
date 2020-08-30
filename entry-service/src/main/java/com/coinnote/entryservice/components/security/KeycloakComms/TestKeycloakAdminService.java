package com.coinnote.entryservice.components.security.KeycloakComms;


import com.coinnote.entryservice.components.security.rolemgmt.KeycloakServicesRole;
import com.coinnote.entryservice.exception.CoinnoteException;
import lombok.AllArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Profile("test")
public class TestKeycloakAdminService extends KeycloakAdminService {

    public TestKeycloakAdminService(KeycloakAccessSettings keycloakAccessSettings,
                                    Keycloak keycloakAdmin,
                                    KeycloakServicesRole keycloakServicesRole) {
        super(keycloakAccessSettings, keycloakAdmin, keycloakServicesRole);
    }

    /**
     * Get userName for principal security context holder.
     * @return
     */
    @Override
    public String getPrincipalUserName(){
        return "testUser";
    }
}
