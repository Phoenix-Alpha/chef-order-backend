package com.halalhomemade.backend.dtos.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.models.FoodAllergen;
import com.halalhomemade.backend.models.FoodCuisine;
import com.halalhomemade.backend.models.FoodTag;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.OfferType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChefOfferDetailRequest {

  @Size(max = 254)
  private String email;
  
  @NotNull
  private Long offerId;
  
  private OfferStatus status;
  
  private OfferType offerType;
  
  @Size(max = 32)
  private String title;
  
  @Size(max = 64)
  private String description;
  
  private Integer weight;
  
  @Size(max = 128)
  private String servingAddress;
  
  @Size(max = 16)
  private String servingPostcode;
  
  @Size(max = 64)
  private String servingCity;
  
  private Instant servingStart;
  
  private Instant servingEnd;
  
  private BigDecimal price;
  
  private Integer maxQuantity;
  
  private Integer quantityAvailable;
  
  private Boolean isPickup;
  
  private Boolean isDelivery;
  
  private Integer minPreorderHours;
  
  private BigDecimal zone1MaxDistance;
  
  private BigDecimal zone1DeliveryPrice;
  
  private BigDecimal zone2MaxDistance;
    
  private BigDecimal zone2DeliveryPrice;
  
  private BigDecimal zone3MaxDistance;
  
  private BigDecimal zone3DeliveryPrice;
  
  @Size(max = 256)
  private String offerPicture1;
  
  @Size(max = 256)
  private String offerPicture2;
  
  @Size(max = 256)
  private String offerPicture3;
  
  private List<String> allergens;
  
  private List<String> cuisines;
  
  private List<String> tags;
  
}
