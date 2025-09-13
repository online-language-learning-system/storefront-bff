package com.hub.storefront_bff.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("http://localhost:3000");            // Local FE
        config.addAllowedOriginPattern("http://storefront-reactjs:80");     // Container FE
        config.addAllowedOriginPattern("http://identity:8080");
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);   // Allow cookie or Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

}
