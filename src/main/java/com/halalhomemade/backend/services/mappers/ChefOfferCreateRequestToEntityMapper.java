package com.halalhomemade.backend.services.mappers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.GeocodingResult;
import com.halalhomemade.backend.dtos.request.ChefOfferCreateRequest;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.models.Offer.OfferBuilder;
import com.halalhomemade.backend.repositories.AllergenOfferRepository;
import com.halalhomemade.backend.repositories.AllergenRepository;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.CuisineChefRepository;
import com.halalhomemade.backend.repositories.CuisineOfferRepository;
import com.halalhomemade.backend.repositories.CuisineRepository;
import com.halalhomemade.backend.repositories.TagOfferRepository;
import com.halalhomemade.backend.repositories.TagRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.ChefOfferService;
import com.halalhomemade.backend.services.GoogleGeoApiService;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChefOfferCreateRequestToEntityMapper implements Function<ChefOfferCreateRequest, Offer> {

	@Autowired UserRepository userRepository;
	@Autowired ChefRepository chefRepository;
	@Autowired CuisineRepository cuisineRepository;
	@Autowired TagRepository tagRepository;
	@Autowired AllergenRepository allergenRepository;
	
	@Autowired GoogleGeoApiService googleGeoApiService;
	
	@Override
	public Offer apply(ChefOfferCreateRequest request) {
		try {
			User user = userRepository.findOneByEmail(request.getEmail()).get();
			Chef chef = chefRepository.findOneByUserId(user.getId()).get();
			if (request.getStatus().equals(OfferStatus.DRAFT)) {
				OfferBuilder offerBuilder = Offer.builder()
						.offerType(request.getOfferType())
						.status(request.getStatus())
						.isPickup(request.getIsDelivery())
						.isDelivery(request.getIsDelivery());
				
				Optional<String> title = Optional.ofNullable(request.getTitle());
				if (title.isPresent()) {
					offerBuilder.title(title.get());
				}
				
				Optional<String> description = Optional.ofNullable(request.getDescription());
				if (description.isPresent()) {
					offerBuilder.description(description.get());
				}
				
				Optional<Integer> maxQuantity = Optional.ofNullable(request.getMaxQuantity());
				if (maxQuantity.isPresent()) {
					offerBuilder.maxQuantity(maxQuantity.get());
					offerBuilder.quantityAvailable(maxQuantity.get());
				}
				
				Optional<Integer> minPreorderHours = Optional.ofNullable(request.getMinPreorderHours());
				if (minPreorderHours.isPresent()) {
					offerBuilder.minPreorderHours(minPreorderHours.get());
				}
				
				Optional<BigDecimal> price = Optional.ofNullable(request.getPrice());
				if (price.isPresent()) {
					offerBuilder.price(price.get());
				}
				
				Optional<String> servingAddress = Optional.ofNullable(request.getServingAddress());
				if (servingAddress.isPresent()) {
					offerBuilder.servingAddress(servingAddress.get());
				}
				
				Optional<String> servingPostcode = Optional.ofNullable(request.getServingPostcode());
				if (servingPostcode.isPresent()) {
					offerBuilder.servingPostcode(servingPostcode.get());
				}
				
				Optional<String> servingCity = Optional.ofNullable(request.getServingCity());
				if (servingCity.isPresent()) {
					offerBuilder.servingCity(servingCity.get());
				}
				
				// Get latitude & longitude from address using Google geocoding api...
				if (servingAddress.isPresent() && servingCity.isPresent() && servingPostcode.isPresent()) {
					String fullAddress = servingAddress.get() + ", " + servingCity.get() + " " + servingPostcode.get();
					Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(fullAddress));
					if (geoResult.isPresent()) {
						offerBuilder.latitude(geoResult.get().getLatitude());
						offerBuilder.longitude(geoResult.get().getLongitude());
					}
				}
				
				Optional<Instant> servingStart = Optional.ofNullable(request.getServingStart());
				if (servingStart.isPresent()) {
					offerBuilder.servingStart(servingStart.get());
				}
				
				Optional<Instant> servingEnd = Optional.ofNullable(request.getServingEnd());
				if (servingEnd.isPresent()) {
					offerBuilder.servingEnd(servingEnd.get());
				}
				
				Optional<Integer> weight = Optional.ofNullable(request.getWeight());
				if (weight.isPresent()) {
					offerBuilder.weight(weight.get());
				}
				
				Optional<BigDecimal> minFreeDeliveryAmount = Optional.ofNullable(request.getMinFreeDeliveryAmount());
				if (minFreeDeliveryAmount.isPresent()) {
					offerBuilder.minFreeDeliveryAmount(minFreeDeliveryAmount.get());
				}
				
				Optional<String> offerPicture1 = Optional.ofNullable(request.getOfferPicture1());
				if (offerPicture1.isPresent()) {
					offerBuilder.offerPicture1(offerPicture1.get());
				}
				
				Optional<String> offerPicture2 = Optional.ofNullable(request.getOfferPicture2());
				if (offerPicture2.isPresent()) {
					offerBuilder.offerPicture2(offerPicture2.get());
				}
				
				Optional<String> offerPicture3 = Optional.ofNullable(request.getOfferPicture3());
				if (offerPicture3.isPresent()) {
					offerBuilder.offerPicture3(offerPicture3.get());
				}
				
				Optional<BigDecimal> zone1MaxDistance = Optional.ofNullable(request.getZone1MaxDistance());
				if (zone1MaxDistance.isPresent()) {
					offerBuilder.zone1MaxDistance(zone1MaxDistance.get());
				}
				
				Optional<BigDecimal> zone1DeliveryPrice = Optional.ofNullable(request.getZone1DeliveryPrice());
				if (zone1DeliveryPrice.isPresent()) {
					offerBuilder.zone1DeliveryPrice(zone1DeliveryPrice.get());
				}
				
				Optional<BigDecimal> zone2MaxDistance = Optional.ofNullable(request.getZone2MaxDistance());
				if (zone2MaxDistance.isPresent()) {
					offerBuilder.zone2MaxDistance(zone2MaxDistance.get());
				}
				
				Optional<BigDecimal> zone2DeliveryPrice = Optional.ofNullable(request.getZone2DeliveryPrice());
				if (zone2DeliveryPrice.isPresent()) {
					offerBuilder.zone2DeliveryPrice(zone2DeliveryPrice.get());
				}
				
				Optional<BigDecimal> zone3MaxDistance = Optional.ofNullable(request.getZone3MaxDistance());
				if (zone3MaxDistance.isPresent()) {
					offerBuilder.zone3MaxDistance(zone3MaxDistance.get());
				}
				
				Optional<BigDecimal> zone3DeliveryPrice = Optional.ofNullable(request.getZone3DeliveryPrice());
				if (zone3DeliveryPrice.isPresent()) {
					offerBuilder.zone3DeliveryPrice(zone3DeliveryPrice.get());
				}
				
				Offer offer = offerBuilder.build();
				offer.setChef(chef);
				
				request.getAllergens().forEach(allergenName -> {
					Optional<Allergen> allergen = allergenRepository.findOneByName(FoodAllergen.valueOf(allergenName));
					if (allergen.isPresent()) {
						offer.addAllergen(allergen.get());	
					}
				});
				
				request.getCuisines().forEach(cuisineName -> {
					Optional<Cuisine> cuisine = cuisineRepository.findOneByName(FoodCuisine.valueOf(cuisineName));
					if (cuisine.isPresent()) {
						offer.addCuisine(cuisine.get());	
					}
				});
				
				request.getTags().forEach(tagName -> {
					Optional<Tag> tag = tagRepository.findOneByName(FoodTag.valueOf(tagName));
					if (tag.isPresent()) {
						offer.addTag(tag.get());
					}
				});
				
				return offer;
				
				
			} else if (request.getStatus().equals(OfferStatus.ACTIVE)) {
				
				OfferBuilder offerBuilder = Offer.builder()
						.offerType(request.getOfferType())
						.status(request.getStatus())
						.isPickup(request.getIsDelivery())
						.isDelivery(request.getIsDelivery());
				
				if (request.getOfferType().equals(OfferType.PREORDER)) {
					Optional<Instant> servingStart = Optional.ofNullable(request.getServingStart());
					Optional<Instant> servingEnd = Optional.ofNullable(request.getServingEnd());
					Optional<Instant> orderUntil = Optional.ofNullable(request.getOrderUntil());
					
					if (servingStart.isPresent() && servingEnd.isPresent() && orderUntil.isPresent()) {
						offerBuilder.servingStart(servingStart.get()).servingEnd(servingEnd.get()).orderUntil(orderUntil.get());
					} else {
						throw new Exception("Serving date not present");
					}
					
					Optional<Integer> maxQuantity = Optional.ofNullable(request.getMaxQuantity());
					if (maxQuantity.isPresent()) {
						offerBuilder.maxQuantity(maxQuantity.get()).quantityAvailable(maxQuantity.get());
					} else {
						throw new Exception("Max quantity not present");
					}
					
				} else if (request.getOfferType().equals(OfferType.ONDEMAND)) {
					Optional<Integer> minPreorderHours = Optional.ofNullable(request.getMinPreorderHours());
					if (minPreorderHours.isPresent()) {
						offerBuilder.minPreorderHours(minPreorderHours.get());
					} else {
						throw new Exception("Minimum pre-order hours not present");
					}
				} else {
					throw new Exception("Invalid offer type");
				}

				Optional<String> title = Optional.ofNullable(request.getTitle());
				if (title.isPresent()) {
					offerBuilder.title(title.get());
				} else {
					throw new Exception("Offer title not present!");
				}
				
				Optional<String> description = Optional.ofNullable(request.getDescription());
				if (description.isPresent()) {
					offerBuilder.description(description.get());
				} else {
					throw new Exception("Offer description not present!");
				}
				
				Optional<Integer> weight = Optional.ofNullable(request.getWeight());
				if (weight.isPresent()) {
					offerBuilder.weight(weight.get());
				} else {
					throw new Exception("Offer weight not present");
				}
				
				Optional<BigDecimal> price = Optional.ofNullable(request.getPrice());
				if (price.isPresent()) {
					offerBuilder.price(price.get());
				} else {
					throw new Exception("Price not present!");
				}
				
				Optional<String> offerPicture1 = Optional.ofNullable(request.getOfferPicture1());
				if (offerPicture1.isPresent()) {
					offerBuilder.offerPicture1(offerPicture1.get());
				} else {
					throw new Exception("Offer picture not present!");
				}
						
				Optional<String> offerPicture2 = Optional.ofNullable(request.getOfferPicture2());
				if (offerPicture2.isPresent()) {
					offerBuilder.offerPicture2(offerPicture2.get());
				}
				
				Optional<String> offerPicture3 = Optional.ofNullable(request.getOfferPicture3());
				if (offerPicture3.isPresent()) {
					offerBuilder.offerPicture3(offerPicture3.get());
				}
				
				if (!request.getIsDelivery() && !request.getIsPickup()) {
					throw new Exception("Invalid delivery type");
				}
				
				if (request.getIsPickup()) {
					
					offerBuilder.isPickup(request.getIsPickup());
					
					Optional<String> servingAddress = Optional.ofNullable(request.getServingAddress());
					Optional<String> servingPostcode = Optional.ofNullable(request.getServingPostcode());
					Optional<String> servingCity = Optional.ofNullable(request.getServingCity());
					if (servingAddress.isPresent() && servingPostcode.isPresent() && servingCity.isPresent()) {
						offerBuilder.servingAddress(servingAddress.get()).servingCity(servingCity.get()).servingPostcode(servingPostcode.get());
						
						// Get latitude & longitude from address using Google geocoding api...
						String fullAddress = servingAddress.get() + ", " + servingCity.get() + " " + servingPostcode.get();
						Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(fullAddress));
						if (geoResult.isPresent()) {
							offerBuilder.latitude(geoResult.get().getLatitude());
							offerBuilder.longitude(geoResult.get().getLongitude());
						}
						
					} else {
						throw new Exception("Serving address not present");
					}
				}
				
				if (request.getIsDelivery()) {
					
					offerBuilder.isDelivery(request.getIsDelivery());
					
					Optional<BigDecimal> zone1MaxDistance = Optional.ofNullable(request.getZone1MaxDistance());
					Optional<BigDecimal> zone1DeliveryPrice = Optional.ofNullable(request.getZone1DeliveryPrice());
					if (zone1MaxDistance.isPresent() && zone1DeliveryPrice.isPresent()) {
						offerBuilder.zone1MaxDistance(zone1MaxDistance.get()).zone1DeliveryPrice(zone1DeliveryPrice.get());
					} else {
						throw new Exception("Delivery zone information not present");
					}
					
					Optional<BigDecimal> zone2MaxDistance = Optional.ofNullable(request.getZone2MaxDistance());
					Optional<BigDecimal> zone2DeliveryPrice = Optional.ofNullable(request.getZone2DeliveryPrice());
					if (zone2MaxDistance.isPresent() && zone2DeliveryPrice.isPresent()) {
						offerBuilder.zone2MaxDistance(zone2MaxDistance.get()).zone2DeliveryPrice(zone2DeliveryPrice.get());
					}
					
					Optional<BigDecimal> zone3MaxDistance = Optional.ofNullable(request.getZone3MaxDistance());
					Optional<BigDecimal> zone3DeliveryPrice = Optional.ofNullable(request.getZone3DeliveryPrice());
					if (zone3MaxDistance.isPresent() && zone3DeliveryPrice.isPresent()) {
						offerBuilder.zone3MaxDistance(zone3MaxDistance.get()).zone3DeliveryPrice(zone3DeliveryPrice.get());
					}
					
					Optional<BigDecimal> minFreeDeliveryAmount = Optional.ofNullable(request.getMinFreeDeliveryAmount());
					if (minFreeDeliveryAmount.isPresent()) {
						offerBuilder.minFreeDeliveryAmount(minFreeDeliveryAmount.get());
					}
				}
				
				Offer offer = offerBuilder.build();
				offer.setChef(chef);
				
				request.getAllergens().forEach(allergenName -> {
					Optional<Allergen> allergen = allergenRepository.findOneByName(FoodAllergen.valueOf(allergenName));
					if (allergen.isPresent()) {
						offer.addAllergen(allergen.get());	
					}
				});
				
				request.getCuisines().forEach(cuisineName -> {
					Optional<Cuisine> cuisine = cuisineRepository.findOneByName(FoodCuisine.valueOf(cuisineName));
					if (cuisine.isPresent()) {
						offer.addCuisine(cuisine.get());	
					}
				});
				
				request.getTags().forEach(tagName -> {
					Optional<Tag> tag = tagRepository.findOneByName(FoodTag.valueOf(tagName));
					if (tag.isPresent()) {
						offer.addTag(tag.get());
					}
				});
				return offer;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}
