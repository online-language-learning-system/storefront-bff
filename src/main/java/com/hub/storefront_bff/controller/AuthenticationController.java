package com.hub.storefront_bff.controller;

import com.hub.storefront_bff.viewmodel.AuthenticatedUserVm;
import com.hub.storefront_bff.viewmodel.AuthenticationInfoVm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

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

}
