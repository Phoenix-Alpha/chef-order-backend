package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.models.IPublicOffer;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse.PublicOfferResponseBuilder;
import com.halalhomemade.backend.models.AllergenOffer;
import com.halalhomemade.backend.models.CuisineOffer;
import com.halalhomemade.backend.models.FoodAllergen;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.models.FoodTag;
import com.halalhomemade.backend.models.OfferDeliveryZone;
import com.halalhomemade.backend.models.TagOffer;
import com.halalhomemade.backend.repositories.AllergenOfferRepository;
import com.halalhomemade.backend.repositories.CuisineOfferRepository;
import com.halalhomemade.backend.repositories.TagOfferRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IPublicOfferToPublicOfferResponseMapper implements Function<IPublicOffer, PublicOfferResponse> {

	@Autowired AllergenOfferRepository allergenOfferRepository;
	@Autowired CuisineOfferRepository cuisineOfferRepository;
	@Autowired TagOfferRepository tagOfferRepository;
	
	@Override
	public PublicOfferResponse apply(IPublicOffer o) {
		
		Set<FoodAllergen>allergens = new HashSet<FoodAllergen>();
		List<AllergenOffer> allergenOffers = allergenOfferRepository.findAllByOfferId(o.getOfferId());
		allergenOffers.forEach(allergenOffer -> {
			allergens.add(allergenOffer.getAllergen().getName());
		});
		
		Set<FoodCuisine>cuisines = new HashSet<FoodCuisine>();
		List<CuisineOffer> cuisineOffers = cuisineOfferRepository.findAllByOfferId(o.getOfferId());
		cuisineOffers.forEach(cuisineOffer -> {
			cuisines.add(cuisineOffer.getCuisine().getName());
		});
		
		Set<FoodTag>tags = new HashSet<FoodTag>();
		List<TagOffer> tagOffers = tagOfferRepository.findAllByOfferId(o.getOfferId());
		tagOffers.forEach(tagOffer -> {
			tags.add(tagOffer.getTag().getName());
		});
		
		Set<String>offerPictures = new HashSet<String>();
		Optional<String> offerPicture1 = Optional.ofNullable(o.getOfferPicture1());
		if (offerPicture1.isPresent()) {
			offerPictures.add(offerPicture1.get());
		}
		Optional<String> offerPicture2 = Optional.ofNullable(o.getOfferPicture2());
		if (offerPicture2.isPresent()) {
			offerPictures.add(offerPicture2.get());
		}
		Optional<String> offerPicture3 = Optional.ofNullable(o.getOfferPicture3());
		if (offerPicture3.isPresent()) {
			offerPictures.add(offerPicture3.get());
		}
		
		Set<OfferDeliveryZone> zones = new HashSet<OfferDeliveryZone>();
		Optional<BigDecimal> zone1MaxDistance = Optional.ofNullable(o.getZone1MaxDistance());
		Optional<BigDecimal> zone1DeliveryPrice = Optional.ofNullable(o.getZone1DeliveryPrice());
		if (zone1MaxDistance.isPresent() && zone1DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone1MaxDistance.get())
					.deliveryPrice(zone1DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		Optional<BigDecimal> zone2MaxDistance = Optional.ofNullable(o.getZone2MaxDistance());
		Optional<BigDecimal> zone2DeliveryPrice = Optional.ofNullable(o.getZone2DeliveryPrice());
		if (zone2MaxDistance.isPresent() && zone2DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone2MaxDistance.get())
					.deliveryPrice(zone2DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		Optional<BigDecimal> zone3MaxDistance = Optional.ofNullable(o.getZone3MaxDistance());
		Optional<BigDecimal> zone3DeliveryPrice = Optional.ofNullable(o.getZone3DeliveryPrice());
		if (zone3MaxDistance.isPresent() && zone3DeliveryPrice.isPresent()) {
			OfferDeliveryZone zone = OfferDeliveryZone.builder()
					.maxDistance(zone3MaxDistance.get())
					.deliveryPrice(zone3DeliveryPrice.get())
					.build();
			zones.add(zone);
		}
		
		
		PublicOfferResponseBuilder publicOfferResponseBuilder = PublicOfferResponse.builder()
				.chefId(o.getChefId())
				.chefProfileName(o.getChefProfileName())
				.chefProfilePicture(o.getChefProfilePicture())
				.chefRating(o.getChefRating())
				.chefTotalReviews(o.getChefTotalReviews())
				.offerId(o.getOfferId())
				.title(o.getTitle())
				.description(o.getDescription())
				.maxQuantity(o.getMaxQuantity())
				.quantityAvailable(o.getQuantityAvailable())
				.minPreorderHours(o.getMinPreorderHours())
				.minFreeDeliveryAmount(o.getMinFreeDeliveryAmount())
				.status(o.getStatus())
				.offerType(o.getOfferType())
				.isDelivery(o.getIsDelivery())
				.isPickup(o.getIsPickup())
				.price(o.getPrice())
				.priceScore(o.getPriceScore())
				.servingAddress(o.getServingAddress())
				.servingCity(o.getServingCity())
				.servingPostcode(o.getServingPostcode())
				.servingStart(o.getServingStart())
				.servingEnd(o.getServingEnd())
				.orderUntil(o.getOrderUntil())
				.dateDiff(o.getDateDiff())
				.dateScore(o.getDateScore())
				.deliveryCost(o.getDeliveryCost())
				.deliveryCostScore(o.getDeliveryCostScore())
				.weight(o.getWeight())
				.latitude(o.getLatitude())
				.longitude(o.getLongitude())
				.distance(o.getDistance())
				.distanceScore(o.getDistanceScore())
				.totalScore(o.getTotalScore())
				.allergens(allergens)
				.cuisines(cuisines)
				.tags(tags)
				.offerPictures(offerPictures)
				.zones(zones);
		
		return publicOfferResponseBuilder.build();
	}
	
}

