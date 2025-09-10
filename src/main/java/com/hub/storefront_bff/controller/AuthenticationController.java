package com.hub.storefront_bff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hub.storefront_bff.dto.AuthenticatedUserVm;
import com.hub.storefront_bff.dto.AuthenticationInfoVm;
import com.hub.storefront_bff.dto.TokenResponseDto;
import com.hub.storefront_bff.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final TokenService tokenService;

    public AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/authentication")
    public ResponseEntity<AuthenticationInfoVm> user(@AuthenticationPrincipal OAuth2User principal) {

    /*
        -- pure Spring Security
        -- The issue:
            If using OAuth2/OIDC
                => principal is not UserDetails, is able to be Jwt or OidcUser


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails == null)
            return ResponseEntity.ok(new AuthenticationInfoVm(false, null));

        AuthenticatedUserVm authenticatedUserVm = new AuthenticatedUserVm(userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationInfoVm(true, authenticatedUserVm));
    */

        if (principal == null)
            return ResponseEntity.ok(new AuthenticationInfoVm(false, null));

        AuthenticatedUserVm authenticatedUserVm =
                new AuthenticatedUserVm(principal.getAttribute("preferred_username"));
        return ResponseEntity.ok(new AuthenticationInfoVm(true, authenticatedUserVm));
    }

    @GetMapping("/get-token")
    public TokenResponseDto getToken() throws JsonProcessingException {
        return tokenService.getToken();
    }
}
