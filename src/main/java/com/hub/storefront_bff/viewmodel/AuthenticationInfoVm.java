package com.hub.storefront_bff.viewmodel;

public record AuthenticationInfoVm(
        boolean isAuthenticated, AuthenticatedUserVm authenticatedUser
) {
}
