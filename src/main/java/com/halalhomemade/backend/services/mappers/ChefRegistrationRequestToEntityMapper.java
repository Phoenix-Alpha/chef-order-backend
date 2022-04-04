package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.ChefRegistrationRequest;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.CuisineChefRepository;
import com.halalhomemade.backend.repositories.CuisineRepository;
import com.halalhomemade.backend.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChefRegistrationRequestToEntityMapper implements Function<ChefRegistrationRequest, Chef> {

	@Autowired UserRepository userRepository;
	@Autowired CuisineRepository cuisineRepository;
	@Autowired CuisineChefRepository cuisineChefRepository;
	
	@Override
	public Chef apply(ChefRegistrationRequest request) {
		User user = userRepository.findOneByEmail(request.getEmail()).get();
		Chef chef = Chef.builder()
				.status(ChefStatus.UNCONFIRMED)
				.profilePicture(request.getProfilePicture())
				.profileName(request.getProfileName())
				.aboutMe(request.getAboutMe())
				.sellPlan(request.getSellPlan())
				.rating(BigDecimal.ZERO)
				.activeOffers(0)
				.totalReviews(0)
				.mealsServed(0)
				.build();
		chef.setUser(user);
		request.getCuisines().forEach(cuisineName -> {
			Optional<Cuisine> cuisine = cuisineRepository.findOneByName(FoodCuisine.valueOf(cuisineName));
			if (cuisine.isPresent()) {
				chef.addCuisine(cuisine.get());	
			}
		});
		return chef;
	}
}
