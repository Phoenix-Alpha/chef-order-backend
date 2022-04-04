package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.PublicFetchOfferDetailRequest;
import com.halalhomemade.backend.dtos.request.PublicSearchOffersRequest;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.dtos.response.PublicOfferSuggestResponse;
import com.halalhomemade.backend.dtos.response.PublicOffersResponse;
import com.halalhomemade.backend.services.PublicOfferService;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/offer")
@Validated
public class PublicOfferController extends BaseController {

	@Autowired
	private PublicOfferService publicOfferService;
	
	@PostMapping(
			value = "/search",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicOffersResponse> getOffers(@RequestBody final @Valid  PublicSearchOffersRequest request, Pageable pageable) {
		return publicOfferService.searchOffers(request, pageable);
	}
	
	@PostMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicOfferResponse> getPublicOfferDetail(@RequestBody final @Valid  PublicFetchOfferDetailRequest request) {
		return publicOfferService.getPublicOfferDetail(request);
	}
	
	@GetMapping(
			value = "/top-offers",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublicOfferSuggestResponse>> getTopOffers() {
		return publicOfferService.getTopOffers();
	}
	
	@GetMapping(
			value = "/suggest",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublicOfferSuggestResponse>> getOfferSuggests(@RequestParam(required = true) String offerName) {
		return publicOfferService.getOfferSuggests(offerName);
	}
	
}
