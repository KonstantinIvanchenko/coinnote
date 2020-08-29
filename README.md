# coinnote
Back-end for the Coinnote app.
Implements Muliservice pattern: 
--entry-service - manages client requests.
--history-service - internal (external requests forbidden).
--auth-service (deprecated); Keycloak Auth server used instead.

Uses: Spring Boot (Data MongoDB, Security), Lombok, Mapstruct, Keycloak
