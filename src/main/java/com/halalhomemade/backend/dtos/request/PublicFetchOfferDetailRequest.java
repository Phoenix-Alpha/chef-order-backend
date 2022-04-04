package com.halalhomemade.backend.dtos.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicFetchOfferDetailRequest {

  @NotNull
  private Long offerId;
  
  @NotNull
  private String userAddress;
  
}
