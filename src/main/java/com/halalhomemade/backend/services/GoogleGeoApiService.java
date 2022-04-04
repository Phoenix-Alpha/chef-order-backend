package com.halalhomemade.backend.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.halalhomemade.backend.models.Position;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleGeoApiService {
	
	@Autowired GeoApiContext geoApiContext;
	
	public Position getGeoCodingResult(String address) {
		GeocodingResult[] result;
		try {
			result = GeocodingApi.geocode(geoApiContext, address).await();
			if (result.length > 0) {
				log.error(result[0].geometry.toString());
				GeocodingResult geoResult = result[0];
				return Position.builder()
					.latitude(BigDecimal.valueOf(geoResult.geometry.location.lat))
					.longitude(BigDecimal.valueOf(geoResult.geometry.location.lng))
					.build();
			} else {
				return null;
			}
		} catch (ApiException | InterruptedException | IOException e) {
			log.error(e.toString());
			return null;
		}
	}
	
	

}