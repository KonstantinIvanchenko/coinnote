package com.coinnote.authservice.auth;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class SecurityConfiguration {

    @Configuration
    @EnableWebSecurity
    protected static class WebSecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsService userDetailsService;
        //TODO: implement this

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

            KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();

            //TODO: check this
            keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(keycloakAuthenticationProvider);

            auth.userDetailsService(userDetailsService);
        }

        @Override
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(
                    new SessionRegistryImpl());
        }


        //TODO:tbd this
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }
    }



}

