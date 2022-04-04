package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.UpdateChefDetailRequest;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.CuisineRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.LanguageService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateChefDetailRequestToEntityMapper implements Function<UpdateChefDetailRequest, Chef> {

	
	@Autowired private UserRepository userRepository;
	@Autowired private ChefRepository chefRepository;
	@Autowired private CuisineRepository cuisineRepository;
	@Autowired private LanguageService languageService;
  
	@Override
	public Chef apply(UpdateChefDetailRequest request) {
		User user = userRepository.findOneByEmail(request.getEmail()).get();
		
		Chef chef = chefRepository.findOneByUserId(user.getId()).get();
  		
		Optional<String> profileName = Optional.ofNullable(request.getProfileName());
		if (profileName.isPresent()) {
			chef.setProfileName(profileName.get());
		}
		
		Optional<String> aboutMe = Optional.ofNullable(request.getAboutMe());
		if (aboutMe.isPresent()) {
			chef.setAboutMe(aboutMe.get());
		}
		
		Optional<SellPlan> sellPlan = Optional.ofNullable(request.getSellPlan());
		if (sellPlan.isPresent()) {
			chef.setSellPlan(sellPlan.get());
		}
		
		Optional<List<String>> cuisines = Optional.ofNullable(request.getCuisines());
		
		if (cuisines.isPresent()) {
			cuisines.get().forEach(cuisineName -> {
				Optional<Cuisine> cuisine = cuisineRepository.findOneByName(FoodCuisine.valueOf(cuisineName));
				if (cuisine.isPresent()) {
					chef.addCuisine(cuisine.get());	
				}
			});
		}
		return chef;
  }
}
