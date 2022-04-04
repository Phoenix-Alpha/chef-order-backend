package com.halalhomemade.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.maps.GeoApiContext;


@Configuration
public class GoogleGeoApiConfig {
	@Value("${google.geo.api_key}")
    private String apiKey;
	
	@Bean
    public GeoApiContext getGeoApiContext() {
		return new GeoApiContext.Builder()
				.apiKey(apiKey)
			    .build();
	}
	
}
