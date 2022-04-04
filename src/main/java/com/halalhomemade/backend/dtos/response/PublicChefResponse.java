package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.halalhomemade.backend.models.FoodCuisine;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class PublicChefResponse {
  private Long id;
  private String profileName;
  private String profilePicture;
  private String aboutMe;
  private Integer activeOffers;
  private Integer mealsServed;
  private BigDecimal rating;
  private Integer totalReviews;
  private Set<FoodCuisine> cuisines;
  private List<PublicOfferResponse> offers;
}
