package com.coinnote.entryservice.components.security.rolemgmt;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class KeycloakServicesRole {

    @Value("${custom.keycloak.history-service-id}")
    String KeycloakHistoryServiceId;
    @Value("#{'${custom.history-service-userrole-list}'.split(',')}")
    List<String> KeycloakListHistoryServiceRoles;


    /**
     * Used for calling role mapper. Future services to be added here. Realm and user resources shall be invoked
     * outside of this scope. UserId typically generated on user creation within keycloak admin client scope for entry
     * service.
     * @param realmResource
     * @param usersResource
     * @param userId
     */
    public void setServicesRoles(RealmResource realmResource, UsersResource usersResource, String userId){
        setHistoryServicesRolesForUserId(realmResource, usersResource, userId);
    }

    /**
     * Role mapper for history service.
     * @param realmResource
     * @param usersResource
     * @param userId
     */
    private void setHistoryServicesRolesForUserId(RealmResource realmResource, UsersResource usersResource, String userId){
        //Get back UserRepresentation
        UserResource userResource = usersResource.get(userId);

        ClientRepresentation appClient = realmResource.clients() //
                .findByClientId(KeycloakHistoryServiceId).get(0);

        for( String role : KeycloakListHistoryServiceRoles){
            RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId())
                    .roles().get(role).toRepresentation();
            userResource.roles().clientLevel(appClient.getId()).add(Arrays.asList(userClientRole));
        }
    }
}
