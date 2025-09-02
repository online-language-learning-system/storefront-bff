package com.hub.storefront_bff.viewmodel;

public record TokenResponseVm(
        String accessToken,
        String refreshToken
) {
}
