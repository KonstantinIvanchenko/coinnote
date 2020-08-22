package com.coinnote.entryservice.service.auth;

import com.coinnote.entryservice.components.security.KeycloakComms.KeycloakAdminService;
import com.coinnote.entryservice.dto.AuthenticationDto;
import com.coinnote.entryservice.dto.LoginDto;
import com.coinnote.entryservice.dto.UserDto;
import com.coinnote.entryservice.enitity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private static final String realm_user_role = "user";

    //TODO: feign client none
    //FIXME: experimental
    //private final AuthClient authClient;

    private final KeycloakAdminService keycloakAdminService;

    //TODO: experimental
    //private final AuthenticationManager authenticationManager;

    public Boolean isUserPresent(UserDto userDto){
        //TODO: verify this
        if (!keycloakAdminService.getUsersByUsername(userDto.getUserName()).isEmpty())
            return true;

        return false;
    }

    public void signup(UserDto userDto){

        //TEST

        UserRepresentation testUser = keycloakAdminService.getUsersByUsername("mock-user").get(0);

        Map<String, List<String>> roles = testUser.getClientRoles();
        List<String> rroles = testUser.getRealmRoles();

        //~TEST


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
