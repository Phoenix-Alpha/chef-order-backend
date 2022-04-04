package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.models.SellPlan;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ChefResponse {
  private Long id;
  private String userId;
  private String profileName;
  private String profilePicture;
  private String aboutMe;
  private Integer activeOffers;
  private Integer mealsPending;
  private Integer mealsInprep;
  private Integer mealsServed;
  private BigDecimal rating;
  private Integer totalReviews;
  private String referralCode;
  private Set<FoodCuisine> cuisines;
  private SellPlan sellPlan;
  private Instant registeredAt;
}
