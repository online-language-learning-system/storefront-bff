package com.hub.storefront_bff.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
