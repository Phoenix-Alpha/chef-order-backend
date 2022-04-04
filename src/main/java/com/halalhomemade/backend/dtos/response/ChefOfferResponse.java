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
public final class ChefOfferResponse {
  private Long offerId;
  private OfferStatus status;
  private OfferType offerType;
  private String title;
  private String description;
  private Integer weight;
  private String servingAddress;
  private String servingPostcode;
  private String servingCity;
  private Instant servingStart;
  private Instant servingEnd;
  private Instant orderUntil;
  private BigDecimal price;
  private BigDecimal minFreeDeliveryAmount;
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
}
