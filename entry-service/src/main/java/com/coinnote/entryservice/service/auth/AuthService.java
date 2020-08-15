package com.coinnote.entryservice.service.auth;

import com.coinnote.entryservice.components.security.KeycloakComms.KeycloakAdminService;
import com.coinnote.entryservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private static final String realm_user_role = "user";

    //TODO: feign client none
    //FIXME: experimental
    //private final AuthClient authClient;

    private final KeycloakAdminService keycloakAdminService;

    public Boolean isUserPresent(UserDto userDto){
        //TODO: verify this
        if (!keycloakAdminService.getUsersByUsername(userDto.getUserName()).isEmpty())
            return true;

        return false;
    }

    public void signup(UserDto userDto){
        UserRepresentation userRepresentation = new UserRepresentation();
        //Generate id as hash of username and email
        userRepresentation.setId(String.valueOf(userDto.hashCode()));

        String userName = userDto.getUserName();
        userRepresentation.setUsername(userName);
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(new ArrayList<>());
        userRepresentation.setRealmRoles(Arrays.asList(realm_user_role));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userName);
        //TODO: PASSWORD hash it here
        credentialRepresentation.setSecretData(userDto.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.getCredentials().add(credentialRepresentation);

        //TODO: verify this
        keycloakAdminService.saveUserKeycloak(userRepresentation);
        //TODO: require KeycloakBuilder here. No feign client
        //authClient.createUser(userRepresentation);
    }
}
