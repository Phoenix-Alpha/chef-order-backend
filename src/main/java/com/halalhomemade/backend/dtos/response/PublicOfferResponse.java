package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.OfferType;
import com.halalhomemade.backend.models.FoodAllergen;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.models.FoodTag;
import com.halalhomemade.backend.models.OfferDeliveryZone;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class PublicOfferResponse {
	private Long chefId;
	private String chefProfileName;
	private String chefProfilePicture;
	private Integer chefTotalReviews;
	private BigDecimal chefRating;
	
	private Long offerId;
	private OfferStatus status;
	private OfferType offerType;
	private String title;
	private String description;
	private Integer weight;
	
	private String servingAddress;
	private String servingPostcode;
	private String servingCity;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal distance;
	private BigDecimal distanceScore;
	
	private Instant servingStart;
	private Instant servingEnd;
	private Instant orderUntil;
	private BigDecimal dateDiff;
	private BigDecimal dateScore;
	
	private BigDecimal price;
	private BigDecimal priceScore;
	
	private BigDecimal minFreeDeliveryAmount;
	private BigDecimal deliveryCost;
	private BigDecimal deliveryCostScore;
	
	private Integer maxQuantity;
	private Integer quantityAvailable;
	
	private Boolean isPickup;
	private Boolean isDelivery;
	private Integer minPreorderHours;
	
	private Set<OfferDeliveryZone> zones;
	private Set<String> offerPictures;
  	private Set<FoodCuisine> cuisines;
  	private Set<FoodAllergen> allergens;
  	private Set<FoodTag> tags;
  	
  	private BigDecimal totalScore;
  	
}
