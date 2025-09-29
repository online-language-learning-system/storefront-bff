package com.hub.storefront_bff;

import com.hub.storefront_bff.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig.class)
public class StorefrontBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorefrontBffApplication.class, args);
	}

}
