package com.coinnote.entryservice.components.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

//@Configuration
//@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@KeycloakConfiguration
public class KeycloakSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {


    private final KeycloakClientRequestFactory keycloakClientRequestFactory;

    public KeycloakSecurityConfiguration(KeycloakClientRequestFactory keycloakClientRequestFactory) {
        this.keycloakClientRequestFactory = keycloakClientRequestFactory;

        // to use principal and authentication together with @async
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    //Define a Session Authentication Strategy
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    //This is required since Keycloak Spring Boot Adapter 7.0.0 to fetch Keycloak configurations from application.properties
    @Bean
    public KeycloakConfigResolver KeyCloakConfigResolver(){
        return new KeycloakSpringBootConfigResolver();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        super.configure(httpSecurity);
        httpSecurity.authorizeRequests()
                .anyRequest()
                .permitAll();
        httpSecurity.csrf().disable();
    }

    //Register Keycloak as the Authentication Provider
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();

        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        //grantedAuthorityMapper.setPrefix("ROLE_");

        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider);
    }

}
