package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.PublicFetchChefDetailRequest;
import com.halalhomemade.backend.dtos.response.PublicChefResponse;
import com.halalhomemade.backend.dtos.response.PublicChefResponse.PublicChefResponseBuilder;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.services.PublicOfferService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PublicFetchChefDetailRequestToPublicChefResponseMapper implements Function<PublicFetchChefDetailRequest, PublicChefResponse> {

	@Autowired ChefRepository chefRepository;
	@Autowired OfferRepository offerRepository;
	
	@Autowired PublicOfferService publicOfferService;
	
	@Override
	public PublicChefResponse apply(PublicFetchChefDetailRequest request) {
		
		List<PublicOfferResponse> results = publicOfferService.getOfferListByChefId(request.getChefId(), request.getLatitude(), request.getLongitude());
		
		Chef chef = chefRepository.findOneById(request.getChefId()).get();
		
		Set<FoodCuisine>cuisines = new HashSet<FoodCuisine>();
		chef.getCuisines().forEach(cuisine -> {
			cuisines.add(cuisine.getName());
		});
		
		PublicChefResponseBuilder publicChefResponseBuilder = PublicChefResponse.builder()
				.id(chef.getId())
				.profileName(chef.getProfileName())
				.profilePicture(chef.getProfilePicture())
				.aboutMe(chef.getAboutMe())
				.activeOffers(chef.getActiveOffers())
				.mealsServed(chef.getMealsServed())
				.totalReviews(chef.getTotalReviews())
				.rating(chef.getRating())
				.cuisines(cuisines)
				.offers(results);
		
		return publicChefResponseBuilder.build();
	}
	
}
