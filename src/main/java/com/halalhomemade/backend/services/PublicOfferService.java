package com.halalhomemade.backend.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.GeocodingResult;
import com.halalhomemade.backend.models.IPublicOffer;
import com.halalhomemade.backend.models.IPublicOfferMaxMin;
import com.halalhomemade.backend.dtos.request.*;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.dtos.response.PublicOfferSuggestResponse;
import com.halalhomemade.backend.dtos.response.PublicOffersResponse;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.services.mappers.IPublicOfferToPublicOfferResponseMapper;
import com.halalhomemade.backend.services.mappers.OfferEntityToPublicOfferSuggestResponseMapper;
import com.halalhomemade.backend.services.mappers.PublicSearchOffersRequestToSearchCriteria;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.OfferType;
import com.halalhomemade.backend.models.Position;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PublicOfferService {

	@Autowired private OfferRepository offerRepository;
	@Autowired private ChefRepository chefRepository;
	
	@Autowired private PublicSearchOffersRequestToSearchCriteria publicSearchOffersRequestToSearchCriteria;
	
	@Autowired private IPublicOfferToPublicOfferResponseMapper iPublicOfferToPublicOfferResponseMapper;
	
	@Autowired private OfferEntityToPublicOfferSuggestResponseMapper offerEntityToPublicOfferSuggestResponseMapper;
	
	@Autowired private GoogleGeoApiService googleGeoApiService;
	
	public ResponseEntity<List<PublicOfferSuggestResponse>> getTopOffers() {
		try {
			Integer limit = 20;
			List<Offer> results = offerRepository.searchTopOffers(OfferStatus.ACTIVE.toString(), limit);
			return new ResponseEntity<>(results.stream().map(offerEntityToPublicOfferSuggestResponseMapper).collect(Collectors.toList()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public ResponseEntity<List<PublicOfferSuggestResponse>> getOfferSuggests(String offerName) {
		try {
			List<Offer> results = offerRepository.findByTitleLike("%" + offerName + "%");
			return new ResponseEntity<>(results.stream().map(offerEntityToPublicOfferSuggestResponseMapper).collect(Collectors.toList()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<PublicOfferResponse> getPublicOfferDetail(PublicFetchOfferDetailRequest request) {
  		try {
  			Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(request.getUserAddress()));
		    BigDecimal userLat = null;
		    BigDecimal userLon = null;
			if (geoResult.isPresent()) {
				userLat = geoResult.get().getLatitude();
				userLon = geoResult.get().getLongitude();
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
  			Optional<IPublicOffer> existingOffer = offerRepository.searchOfferByIdAndAddress(request.getOfferId(), OfferStatus.ACTIVE.toString(), userLat, userLon);
  			
  			if (existingOffer.isPresent()) {
  				IPublicOffer offer = existingOffer.get();
  			} else {
  				throw new Exception("Offer not exist");
  			}
  			
  			return new ResponseEntity<>(iPublicOfferToPublicOfferResponseMapper.apply(existingOffer.get()), HttpStatus.OK);
  			
  		} catch (Exception e) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}
	
	public List<PublicOfferResponse> getOfferListByChefId(Long chefId, BigDecimal userLat, BigDecimal userLon) {
		
		BigDecimal distanceLimit = new BigDecimal(30.0);
		
		String defaultSortMode = "BESTMATCH";
		
		Instant now = Instant.now();
		
		IPublicOfferMaxMin minMax = offerRepository.findMaxMinForSearchOffer(chefId, null, null, OfferStatus.ACTIVE.toString(), userLat, userLon, distanceLimit, null, null, null, null, null, null, now);
		
		BigDecimal minDateDiff = BigDecimal.ZERO;
		BigDecimal maxDateDiff = BigDecimal.ONE;
		
		BigDecimal minDistance = BigDecimal.ZERO;
		BigDecimal maxDistance = BigDecimal.ONE;
		
		BigDecimal minPrice = BigDecimal.ZERO;
		BigDecimal maxPrice = BigDecimal.ONE;
		
		Optional<BigDecimal> existingMinDateDiff = Optional.ofNullable(minMax.getMinDateDiff());
		Optional<BigDecimal> existingMaxDateDiff = Optional.ofNullable(minMax.getMaxDateDiff());
		Optional<BigDecimal> existingMinDistance = Optional.ofNullable(minMax.getMinDistance());
		Optional<BigDecimal> existingMaxDistance = Optional.ofNullable(minMax.getMaxDistance());
		Optional<BigDecimal> existingMinPrice = Optional.ofNullable(minMax.getMinPrice());
		Optional<BigDecimal> existingMaxPrice = Optional.ofNullable(minMax.getMaxPrice());
		
		if (existingMinDateDiff.isPresent() && existingMaxDateDiff.isPresent()) {
			minDateDiff = existingMinDateDiff.get();
			maxDateDiff = existingMaxDateDiff.get();
		}
		
		if (existingMinDistance.isPresent() && existingMaxDistance.isPresent()) {
			minDistance = existingMinDistance.get();
			maxDistance = existingMaxDistance.get();
		}
		
		if (existingMinPrice.isPresent() && existingMaxPrice.isPresent()) {
			minPrice = existingMinPrice.get();
			maxPrice = existingMaxPrice.get();
		}
				
		List<IPublicOffer> results = offerRepository.searchOffersList(chefId, OfferStatus.ACTIVE.toString(), userLat, userLon, distanceLimit, minDistance, maxDistance, minPrice, maxPrice, null, null, null, null, null, null, null, null, null, defaultSortMode);
		
		return results.stream().map(iPublicOfferToPublicOfferResponseMapper).collect(Collectors.toList());
	}
	
  	
	public ResponseEntity<PublicOffersResponse> searchOffers(PublicSearchOffersRequest request, Pageable pageable) {
  		try {
  			Optional<Instant> servingDate = Optional.ofNullable(request.getServingDate());
		    Instant servingDateFrom = null;
		    Instant servingDateTo = null;
		    if (servingDate.isPresent()) {
		    	servingDateFrom = servingDate.get();
		    	servingDateTo = servingDateFrom.plus(1, ChronoUnit.DAYS);	
		    }
		    
		    String offerTypeStr = null;
			Optional<OfferType> offerType = Optional.ofNullable(request.getOfferType());
			if (offerType.isPresent()) {
				offerTypeStr = offerType.get().toString();
			}
			
			Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(request.getUserAddress()));
		    BigDecimal userLat = null;
		    BigDecimal userLon = null;
			if (geoResult.isPresent()) {
				userLat = geoResult.get().getLatitude();
				userLon = geoResult.get().getLongitude();
				log.error("geoResult");
				log.error(userLat.toString());
				log.error(userLon.toString());
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			Instant now = Instant.now();
			IPublicOfferMaxMin minMax = offerRepository.findMaxMinForSearchOffer(request.getChefId(), request.getChefName(), request.getOfferName(), OfferStatus.ACTIVE.toString(), userLat, userLon, request.getDistance(), offerTypeStr, request.getChefRating(), request.getIsDelivery(), request.getIsPickup(), servingDateFrom, servingDateTo, now);
			
			System.out.println(minMax.getTotalElements());
			System.out.println(minMax.getMaxDistance());
			System.out.println(minMax.getMinDistance());
			System.out.println(minMax.getMaxPrice());
			System.out.println(minMax.getMinPrice());
			
			if (minMax.getTotalElements().equals(0)) {
				List<PublicOfferResponse> emptyResults = new ArrayList<>();
				return new ResponseEntity<>(PublicOffersResponse.builder()
		    		.data(emptyResults)
			        .pageNumber(pageable.getPageNumber())
			        .totalPages(0)
			        .totalElements(0)
			        .pageSize(0)
			        .build(), HttpStatus.OK);
			}
			
			BigDecimal minDateDiff = BigDecimal.ZERO;
			BigDecimal maxDateDiff = BigDecimal.ONE;
			
			BigDecimal minDistance = BigDecimal.ZERO;
			BigDecimal maxDistance = BigDecimal.ONE;
			
			BigDecimal minPrice = BigDecimal.ZERO;
			BigDecimal maxPrice = BigDecimal.ONE;
			
			Optional<BigDecimal> existingMinDateDiff = Optional.ofNullable(minMax.getMinDateDiff());
			Optional<BigDecimal> existingMaxDateDiff = Optional.ofNullable(minMax.getMaxDateDiff());
			Optional<BigDecimal> existingMinDistance = Optional.ofNullable(minMax.getMinDistance());
			Optional<BigDecimal> existingMaxDistance = Optional.ofNullable(minMax.getMaxDistance());
			Optional<BigDecimal> existingMinPrice = Optional.ofNullable(minMax.getMinPrice());
			Optional<BigDecimal> existingMaxPrice = Optional.ofNullable(minMax.getMaxPrice());
			
			if (existingMinDateDiff.isPresent() && existingMaxDateDiff.isPresent()) {
				minDateDiff = existingMinDateDiff.get();
				maxDateDiff = existingMaxDateDiff.get();
			}
			
			if (existingMinDistance.isPresent() && existingMaxDistance.isPresent()) {
				minDistance = existingMinDistance.get();
				maxDistance = existingMaxDistance.get();
			}
			
			if (existingMinPrice.isPresent() && existingMaxPrice.isPresent()) {
				minPrice = existingMinPrice.get();
				maxPrice = existingMaxPrice.get();
			}
			
			Page<IPublicOffer> results = offerRepository.searchOffer(request.getChefId(), request.getChefName(), request.getOfferName(), OfferStatus.ACTIVE.toString(), userLat, userLon, request.getDistance(), minDistance, maxDistance, minPrice, maxPrice, offerTypeStr, request.getChefRating(), request.getIsDelivery(), request.getIsPickup(), servingDateFrom, servingDateTo, now, minDateDiff, maxDateDiff, request.getSortMode().toString(), pageable);
			
			return new ResponseEntity<>(PublicOffersResponse.builder()
	    		.data(results.getContent().stream().map(iPublicOfferToPublicOfferResponseMapper).collect(Collectors.toList()))
		        .pageNumber(results.getNumber())
		        .totalPages(results.getTotalPages())
		        .totalElements(results.getTotalElements())
		        .pageSize(results.getNumberOfElements())
		        .build(), HttpStatus.OK);
  			
  		} catch (Exception e) {
  			log.error(e.toString());
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}
  	
}
