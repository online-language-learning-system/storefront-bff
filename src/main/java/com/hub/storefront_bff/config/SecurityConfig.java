package com.hub.storefront_bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String PREFIX = "ROLE_";

    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    public SecurityConfig(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.reactiveClientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) throws Exception {
        return serverHttpSecurity
                .authorizeExchange(
                    authorizeExchangeSpec ->
                        authorizeExchangeSpec
                            .pathMatchers("/storefront/user/profile").authenticated()
                            .anyExchange().permitAll()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)   // Disable HTTP Basic auth (we only use Bearer JWT tokens)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)             // Disable CSRF when using RestAPI
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                .oauth2Login(Customizer.withDefaults())                 // With Customizer.withDefaults() in OAuth2
                                                                        // OAuth2 Login flow = /oauth2/authorization/{registrationId} to Login URL
                .logout(logoutSpec -> logoutSpec.logoutSuccessHandler(oidcLogoutSuccessHandler()))
                .build();
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler oidcClientInitiatedServerLogoutSuccessHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(this.reactiveClientRegistrationRepository);
        String postLogoutRedirectUri = "{baseUrl}/home"; // {baseUrl} = scheme (http/https) + host + port + contextPath
        oidcClientInitiatedServerLogoutSuccessHandler.setPostLogoutRedirectUri(postLogoutRedirectUri);
        return oidcClientInitiatedServerLogoutSuccessHandler;

    }

    // Role-based access control (need to map authorities from Keycloak to Spring Security)
    @Bean
    @SuppressWarnings("unchecked")
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {

        // GrantedAuthority is a functional interface
        return authorities -> {

            // Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities)
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();  // HashSet -- a set of HashMap
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                OidcUserInfo oidcUserInfo = ((OidcUserAuthority) authority).getUserInfo();

                if (oidcUserInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    Collection<String> roles = (Collection<String>) oidcUserInfo.getClaims().get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }

            } else {
                OAuth2UserAuthority oAuth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes  = oAuth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    var realmAccess = (Map<String, Object>) userAttributes.get(REALM_ACCESS_CLAIM);
                    var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }

            return mappedAuthorities;
        };
    }

    private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(PREFIX + role))
            .collect(Collectors.toList());
    }
}

/*
        If you build a web app with session + cookies, enable CSRF for protection.
        If you build a REST API with JWT/OAuth2, disable CSRF because it's unnecessary
    and will cause errors when calling the API.
*/