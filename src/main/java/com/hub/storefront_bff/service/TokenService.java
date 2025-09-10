package com.hub.storefront_bff.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hub.storefront_bff.config.ServiceUrlConfig;
import com.hub.storefront_bff.dto.TokenResponseDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

    private ServiceUrlConfig serviceUrlConfig;

    public TokenService(ServiceUrlConfig serviceUrlConfig) {
        this.serviceUrlConfig = serviceUrlConfig;
    }


    public TokenResponseDto getToken() throws JsonProcessingException {

        String tokenUrl = serviceUrlConfig.services().get("token-identity");
        String tokenClient = serviceUrlConfig.tokenId();
        String tokenSecret = serviceUrlConfig.tokenSecret();

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        System.out.println(tokenClient);
        System.out.println(tokenSecret);

        // Body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", tokenClient);
        body.add("client_secret", tokenSecret);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            String accessToken = jsonNode.get("access_token").asText();

            //String refreshToken = null;
            //if (jsonNode.get("refresh_token").asBoolean())
            // String refreshToken = jsonNode.get("refresh_token").asText();

            return new TokenResponseDto(accessToken, null);

        } else {
            throw new RuntimeException("Failed to get token: " + response.getStatusCode());
        }
    }

}
