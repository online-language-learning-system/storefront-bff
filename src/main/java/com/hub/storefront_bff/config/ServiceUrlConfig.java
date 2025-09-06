package com.hub.storefront_bff.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(value = "serviceUri")
public record ServiceUrlConfig (
   Map<String, String> services
){ }
