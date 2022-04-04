package com.halalhomemade.backend.dtos.request;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefWalletAddCardRequest {

  @Size(max = 254)
  private String email;
  
  @NotNull
  private String firstName;
	
  @NotNull
  private String lastName;

  @NotNull
  private String cardNumber;
  
  @NotNull
  private Integer cardExpYear;
  
  @NotNull
  private Integer cardExpMonth;
  
  @NotNull
  private String cardCVC;
}
