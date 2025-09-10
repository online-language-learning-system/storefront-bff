package com.hub.storefront_bff.dto;

public record AuthenticationInfoVm(
        boolean isAuthenticated, AuthenticatedUserVm authenticatedUser
) {
}
