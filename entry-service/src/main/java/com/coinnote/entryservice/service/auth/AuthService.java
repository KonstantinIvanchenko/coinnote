package com.coinnote.entryservice.service.auth;

import com.coinnote.entryservice.components.security.KeycloakComms.KeycloakAdminService;
import com.coinnote.entryservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private static final String realm_user_role = "user";
    private final KeycloakAdminService keycloakAdminService;


    public Boolean isUserPresent(UserDto userDto){
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
        credentialRepresentation.setSecretData(userDto.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.getCredentials().add(credentialRepresentation);

        keycloakAdminService.saveUserKeycloak(userRepresentation);
    }

    public String getUserToken(){
        return keycloakAdminService.getUserToken();
    }
}
