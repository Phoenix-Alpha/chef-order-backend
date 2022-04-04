package com.halalhomemade.backend.services.mappers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.maps.model.GeocodingResult;
import com.halalhomemade.backend.dtos.request.PublicSearchOffersRequest;
import com.halalhomemade.backend.models.Position;
import com.halalhomemade.backend.repositories.Specifications.SearchCriteria;
import com.halalhomemade.backend.services.GoogleGeoApiService;

@Component
public class PublicSearchOffersRequestToSearchCriteria implements Function<PublicSearchOffersRequest, List<SearchCriteria>> {

	@Autowired GoogleGeoApiService googleGeoApiService;
    
	@Override
    public List<SearchCriteria> apply(PublicSearchOffersRequest request) {

	    List<SearchCriteria> searchCriteriaList = new ArrayList<>();
	    
	    Optional.ofNullable(request.getOfferType())
	    	.ifPresent(offerType -> searchCriteriaList.add(new SearchCriteria("offerType", offerType)));
	    
	    Optional.ofNullable(request.getChefRating())
			.ifPresent(chefRating -> searchCriteriaList.add(new SearchCriteria("chefRating", chefRating)));
	    
	    Optional.ofNullable(request.getIsDelivery())
			.ifPresent(isDelivery -> searchCriteriaList.add(new SearchCriteria("isDelivery", isDelivery)));
	
	    Optional.ofNullable(request.getIsPickup())
	    	.ifPresent(isPickup -> searchCriteriaList.add(new SearchCriteria("isPickup", isPickup)));
	    
	    searchCriteriaList.add(new SearchCriteria("distance", request.getDistance()));
	    
	    Optional<Instant> servingDate = Optional.ofNullable(request.getServingDate());
	    if (servingDate.isPresent()) {
	    	Instant servingDateFrom = servingDate.get();
	    	Instant servingDateTo = servingDateFrom.plus(1, ChronoUnit.DAYS);
	    	searchCriteriaList.add(new SearchCriteria("servingDateFrom", servingDateFrom));
			searchCriteriaList.add(new SearchCriteria("servingDateTo", servingDateTo));	
	    }
		
	    Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(request.getUserAddress()));
		if (geoResult.isPresent()) {
			searchCriteriaList.add(new SearchCriteria("userLat", geoResult.get().getLatitude()));
			searchCriteriaList.add(new SearchCriteria("userLon", geoResult.get().getLongitude()));
		} else {
			return null;
		}
	    
	    return searchCriteriaList;
	}
}
