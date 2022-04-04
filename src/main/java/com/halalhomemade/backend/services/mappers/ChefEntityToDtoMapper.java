package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.ChefResponse;
import com.halalhomemade.backend.dtos.response.ChefResponse.ChefResponseBuilder;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.FoodCuisine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class ChefEntityToDtoMapper implements Function<Chef, ChefResponse> {

	@Override
	public ChefResponse apply(Chef chef) {
		Set<FoodCuisine>cuisines = new HashSet<FoodCuisine>();
		chef.getCuisines().forEach(cuisine -> {
			cuisines.add(cuisine.getName());
		});
		
		ChefResponseBuilder chefResponseBuilder = ChefResponse.builder()
				.id(chef.getId())
				.userId(chef.getUser().getId().toString())
				.profileName(chef.getProfileName())
				.profilePicture(chef.getProfilePicture())
				.aboutMe(chef.getAboutMe())
				.sellPlan(chef.getSellPlan())
				.activeOffers(chef.getActiveOffers())
				.mealsPending(chef.getMealsPending())
				.mealsInprep(chef.getMealsInprep())
				.mealsServed(chef.getMealsServed())
				.totalReviews(chef.getTotalReviews())
				.rating(chef.getRating())
				.referralCode(chef.getReferralCode())
				.registeredAt(chef.getCreatedAt())
				.cuisines(cuisines);
		
		return chefResponseBuilder.build();
	}
	
}
