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
public class PublicFetchChefDetailRequest {

  @NotNull
  private Long chefId;
  
  @NotNull
  private String userAddress;
  
  private BigDecimal latitude;
  
  private BigDecimal longitude;

}
