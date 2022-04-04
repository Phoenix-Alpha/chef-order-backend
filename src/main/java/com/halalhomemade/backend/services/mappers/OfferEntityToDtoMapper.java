package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.ChefOfferResponse;
import com.halalhomemade.backend.dtos.response.ChefOfferResponse.ChefOfferResponseBuilder;
import com.halalhomemade.backend.models.FoodAllergen;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.models.FoodTag;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.OfferDeliveryZone;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class OfferEntityToDtoMapper implements Function<Offer, ChefOfferResponse> {

	@Override
	public ChefOfferResponse apply(Offer offer) {
		
		Set<FoodAllergen>allergens = new HashSet<FoodAllergen>();
		offer.getAllergens().forEach(allergen -> {
			allergens.add(allergen.getName());
		});
		
		Set<FoodCuisine>cuisines = new HashSet<FoodCuisine>();
		offer.getCuisines().forEach(cuisine -> {
			cuisines.add(cuisine.getName());
		});
		
		Set<FoodTag>tags = new HashSet<FoodTag>();
		offer.getTags().forEach(tag -> {
			tags.add(tag.getName());
		});
		
		Set<String>offerPictures = new HashSet<String>();
		Optional<String> offerPicture1 = Optional.ofNullable(offer.getOfferPicture1());
		if (offerPicture1.isPresent()) {
			offerPictures.add(offerPicture1.get());
		}
		Optional<String> offerPicture2 = Optional.ofNullable(offer.getOfferPicture2());
		if (offerPicture2.isPresent()) {
			offerPictures.add(offerPicture2.get());
		}
		Optional<String> offerPicture3 = Optional.ofNullable(offer.getOfferPicture3());
		if (offerPicture3.isPresent()) {
			offerPictures.add(offerPicture3.get());
		}
		
		Set<OfferDeliveryZone> zones = new HashSet<OfferDeliveryZone>();
		Optional<BigDecimal> zone1MaxDistance = Optional.ofNullable(offer.getZone1MaxDistance());
		Optional<BigDecimal> zone1DeliveryPrice = Optional.ofNullable(offer.getZone1DeliveryPrice());
		if (zone1MaxDistance.isPresent() && zone1DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone1MaxDistance.get())
					.deliveryPrice(zone1DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		Optional<BigDecimal> zone2MaxDistance = Optional.ofNullable(offer.getZone2MaxDistance());
		Optional<BigDecimal> zone2DeliveryPrice = Optional.ofNullable(offer.getZone2DeliveryPrice());
		if (zone2MaxDistance.isPresent() && zone2DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone2MaxDistance.get())
					.deliveryPrice(zone2DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		Optional<BigDecimal> zone3MaxDistance = Optional.ofNullable(offer.getZone3MaxDistance());
		Optional<BigDecimal> zone3DeliveryPrice = Optional.ofNullable(offer.getZone3DeliveryPrice());
		if (zone3MaxDistance.isPresent() && zone3DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone3MaxDistance.get())
					.deliveryPrice(zone3DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		
		ChefOfferResponseBuilder chefOfferResponseBuilder = ChefOfferResponse.builder()
				.offerId(offer.getId())
				.status(offer.getStatus())
				.offerType(offer.getOfferType())
				.isDelivery(offer.getIsDelivery())
				.isPickup(offer.getIsPickup())
				.allergens(allergens)
				.cuisines(cuisines)
				.tags(tags)
				.offerPictures(offerPictures)
				.zones(zones);
		
		Optional<String> title = Optional.ofNullable(offer.getTitle());
		if (title.isPresent()) {
			chefOfferResponseBuilder.title(title.get());
		}
		
		Optional<String> description = Optional.ofNullable(offer.getDescription());
		if (description.isPresent()) {
			chefOfferResponseBuilder.description(description.get());
		}
		
		Optional<Integer> maxQuantity = Optional.ofNullable(offer.getMaxQuantity());
		if (maxQuantity.isPresent()) {
			chefOfferResponseBuilder.maxQuantity(maxQuantity.get());
		}
		
		Optional<Integer> quantityAvailable = Optional.ofNullable(offer.getQuantityAvailable());
		if (quantityAvailable.isPresent()) {
			chefOfferResponseBuilder.quantityAvailable(quantityAvailable.get());
		}
		
		Optional<Integer> minPreorderHours = Optional.ofNullable(offer.getMinPreorderHours());
		if (minPreorderHours.isPresent()) {
			chefOfferResponseBuilder.minPreorderHours(minPreorderHours.get());
		}
		
		Optional<BigDecimal> price = Optional.ofNullable(offer.getPrice());
		if (price.isPresent()) {
			chefOfferResponseBuilder.price(price.get());
		}
		
		Optional<BigDecimal> minFreeDeliveryAmount = Optional.ofNullable(offer.getMinFreeDeliveryAmount());
		if (minFreeDeliveryAmount.isPresent()) {
			chefOfferResponseBuilder.minFreeDeliveryAmount(minFreeDeliveryAmount.get());
		}
		
		Optional<String> servingAddress = Optional.ofNullable(offer.getServingAddress());
		if (servingAddress.isPresent()) {
			chefOfferResponseBuilder.servingAddress(servingAddress.get());
		}
		
		Optional<String> servingPostcode = Optional.ofNullable(offer.getServingPostcode());
		if (servingPostcode.isPresent()) {
			chefOfferResponseBuilder.servingPostcode(servingPostcode.get());
		}
		
		Optional<String> servingCity = Optional.ofNullable(offer.getServingCity());
		if (servingCity.isPresent()) {
			chefOfferResponseBuilder.servingCity(servingCity.get());
		}
		
		Optional<Instant> servingStart = Optional.ofNullable(offer.getServingStart());
		if (servingStart.isPresent()) {
			chefOfferResponseBuilder.servingStart(servingStart.get());
		}
		
		Optional<Instant> servingEnd = Optional.ofNullable(offer.getServingEnd());
		if (servingEnd.isPresent()) {
			chefOfferResponseBuilder.servingEnd(servingEnd.get());
		}
		
		Optional<Instant> orderUntil = Optional.ofNullable(offer.getOrderUntil());
		if (orderUntil.isPresent()) {
			chefOfferResponseBuilder.orderUntil(orderUntil.get());
		}
		
		Optional<Integer> weight = Optional.ofNullable(offer.getWeight());
		if (weight.isPresent()) {
			chefOfferResponseBuilder.weight(weight.get());
		}
		
		return chefOfferResponseBuilder.build();
	}
	
}

