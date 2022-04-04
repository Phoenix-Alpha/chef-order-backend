package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.PublicFetchChefDetailRequest;
import com.halalhomemade.backend.dtos.request.PublicSearchOffersRequest;
import com.halalhomemade.backend.dtos.response.PublicChefResponse;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.dtos.response.PublicOffersResponse;
import com.halalhomemade.backend.dtos.response.PublicSearchChefResponse;
import com.halalhomemade.backend.services.PublicChefService;
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
@RequestMapping("/api/v1/chef")
@Validated
public class PublicChefController extends BaseController {

	@Autowired
	private PublicChefService publicChefService;
	
	@PostMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicChefResponse> getChefDetailWithOfferList(@RequestBody final @Valid  PublicFetchChefDetailRequest request) {
		return publicChefService.getChefDetailWithOfferList(request);
	}
	
	@GetMapping(
			value = "/suggest",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublicSearchChefResponse>> searchChefsByName(@RequestParam(required = true) String chefName) {
		return publicChefService.getChefListFromName(chefName);
	}
	
}
