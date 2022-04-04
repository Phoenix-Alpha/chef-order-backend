package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.UpdateChefOfferDetailRequest;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.AllergenRepository;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.CuisineRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.repositories.TagRepository;
import com.halalhomemade.backend.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateChefOfferDetailRequestToEntityMapper implements Function<UpdateChefOfferDetailRequest, Offer> {

	@Autowired UserRepository userRepository;
	@Autowired ChefRepository chefRepository;
	@Autowired OfferRepository offerRepository;
	@Autowired CuisineRepository cuisineRepository;
	@Autowired TagRepository tagRepository;
	@Autowired AllergenRepository allergenRepository;
	
	@Override
	public Offer apply(UpdateChefOfferDetailRequest request) {
		User user = userRepository.findOneByEmail(request.getEmail()).get();
		Chef chef = chefRepository.findOneByUserId(user.getId()).get();
		
		Offer offer = offerRepository.findOneById(request.getOfferId()).get();
		
		Optional<String> title = Optional.ofNullable(request.getTitle());
		if (title.isPresent()) {
			offer.setTitle(title.get());
		}
		
		Optional<String> description = Optional.ofNullable(request.getDescription());
		if (description.isPresent()) {
			offer.setDescription(description.get());
		}
		
		Optional<OfferType> offerType = Optional.ofNullable(request.getOfferType());
		if (offerType.isPresent()) {
			offer.setOfferType(offerType.get());
		}
		
		Optional<OfferStatus> status = Optional.ofNullable(request.getStatus());
		if (status.isPresent()) {
			offer.setStatus(status.get());
		}
		
		Optional<Boolean> isPickup = Optional.ofNullable(request.getIsPickup());
		if (isPickup.isPresent()) {
			offer.setIsPickup(isPickup.get());
		}
		
		Optional<Boolean> isDelivery = Optional.ofNullable(request.getIsDelivery());
		if (isDelivery.isPresent()) {
			offer.setIsDelivery(isDelivery.get());
		}
		
		Optional<Integer> maxQuantity = Optional.ofNullable(request.getMaxQuantity());
		if (maxQuantity.isPresent()) {
			offer.setMaxQuantity(maxQuantity.get());
		}
		
		Optional<Integer> quantityAvailable = Optional.ofNullable(request.getQuantityAvailable());
		if (quantityAvailable.isPresent()) {
			offer.setQuantityAvailable(quantityAvailable.get());
		}
		
		Optional<Integer> minPreorderHours = Optional.ofNullable(request.getMinPreorderHours());
		if (minPreorderHours.isPresent()) {
			offer.setMinPreorderHours(minPreorderHours.get());
		}
		
		Optional<BigDecimal> price = Optional.ofNullable(request.getPrice());
		if (price.isPresent()) {
			offer.setPrice(price.get());
		}
		
		Optional<String> servingAddress = Optional.ofNullable(request.getServingAddress());
		if (servingAddress.isPresent()) {
			offer.setServingAddress(servingAddress.get());
		}
		
		Optional<String> servingPostcode = Optional.ofNullable(request.getServingPostcode());
		if (servingPostcode.isPresent()) {
			offer.setServingPostcode(servingPostcode.get());
		}
		
		Optional<String> servingCity = Optional.ofNullable(request.getServingCity());
		if (servingCity.isPresent()) {
			offer.setServingCity(servingCity.get());
		}
		
		Optional<Instant> servingStart = Optional.ofNullable(request.getServingStart());
		if (servingStart.isPresent()) {
			offer.setServingStart(servingStart.get());
		}
		
		Optional<Instant> servingEnd = Optional.ofNullable(request.getServingEnd());
		if (servingEnd.isPresent()) {
			offer.setServingEnd(servingEnd.get());
		}
		
		Optional<Integer> weight = Optional.ofNullable(request.getWeight());
		if (weight.isPresent()) {
			offer.setWeight(weight.get());
		}
		
		Optional<String> offerPicture1 = Optional.ofNullable(request.getOfferPicture1());
		if (offerPicture1.isPresent()) {
			offer.setOfferPicture1(offerPicture1.get());
		}
				
		Optional<String> offerPicture2 = Optional.ofNullable(request.getOfferPicture2());
		if (offerPicture2.isPresent()) {
			offer.setOfferPicture2(offerPicture2.get());
		}
		
		Optional<String> offerPicture3 = Optional.ofNullable(request.getOfferPicture3());
		if (offerPicture3.isPresent()) {
			offer.setOfferPicture3(offerPicture3.get());
		}
		
		Optional<BigDecimal> zone1MaxDistance = Optional.ofNullable(request.getZone1MaxDistance());
		Optional<BigDecimal> zone1DeliveryPrice = Optional.ofNullable(request.getZone1DeliveryPrice());
		if (zone1MaxDistance.isPresent() && zone1DeliveryPrice.isPresent()) {
			offer.setZone1MaxDistance(zone1MaxDistance.get());
			offer.setZone1DeliveryPrice(zone1DeliveryPrice.get());
		}
		
		Optional<BigDecimal> zone2MaxDistance = Optional.ofNullable(request.getZone2MaxDistance());
		Optional<BigDecimal> zone2DeliveryPrice = Optional.ofNullable(request.getZone2DeliveryPrice());
		if (zone2MaxDistance.isPresent() && zone2DeliveryPrice.isPresent()) {
			offer.setZone2MaxDistance(zone2MaxDistance.get());
			offer.setZone2DeliveryPrice(zone2DeliveryPrice.get());
		}
		
		Optional<BigDecimal> zone3MaxDistance = Optional.ofNullable(request.getZone3MaxDistance());
		Optional<BigDecimal> zone3DeliveryPrice = Optional.ofNullable(request.getZone3DeliveryPrice());
		if (zone3MaxDistance.isPresent() && zone3DeliveryPrice.isPresent()) {
			offer.setZone3MaxDistance(zone3MaxDistance.get());
			offer.setZone3DeliveryPrice(zone3DeliveryPrice.get());
		}
		
		Optional<List<String>> allergens = Optional.ofNullable(request.getAllergens());
		if (allergens.isPresent()) {
			allergens.get().forEach(allergenName -> {
				Optional<Allergen> allergen = allergenRepository.findOneByName(FoodAllergen.valueOf(allergenName));
				if (allergen.isPresent()) {
					offer.addAllergen(allergen.get());	
				}
			});	
		}
		
		Optional<List<String>> cuisines = Optional.ofNullable(request.getCuisines());
		if (cuisines.isPresent()) {
			cuisines.get().forEach(cuisineName -> {
				Optional<Cuisine> cuisine = cuisineRepository.findOneByName(FoodCuisine.valueOf(cuisineName));
				if (cuisine.isPresent()) {
					offer.addCuisine(cuisine.get());	
				}
			});	
		}
		
		Optional<List<String>> tags = Optional.ofNullable(request.getTags());
		if (tags.isPresent()) {
			tags.get().forEach(tagName -> {
				Optional<Tag> tag = tagRepository.findOneByName(FoodTag.valueOf(tagName));
				if (tag.isPresent()) {
					offer.addTag(tag.get());
				}
			});	
		}
		
		return offer;
	}
}
