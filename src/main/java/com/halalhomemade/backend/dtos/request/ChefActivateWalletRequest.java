package com.halalhomemade.backend.dtos.request;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.models.AuthenticationMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefActivateWalletRequest {

  @Size(max = 254)
  private String email;
  
//  @NotNull
//  private String legalFirstName;
//  
//  @NotNull
//  private String legalLastName;
//  
//  @NotNull
//  private String billingAddressCity;
//  
//  @NotNull
//  private String billingAddressPostcode;
//  
//  @NotNull
//  private String billingAddressLine;
//  
//  @NotNull
//  private Instant birthday;
//  
//  @NotNull
//  private String accountNumber;
//  
//  private String ip;
  
}
