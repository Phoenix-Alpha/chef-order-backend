package com.halalhomemade.backend.dtos.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.constants.IValidationConstants;
import com.halalhomemade.backend.models.AuthenticationMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFetchOrRegisterRequest {

  @NotBlank
  @Size(max = 254)
  @Email
  private String email;
  
  @NotBlank
  @Size(max = 32)
  private String firstName;

  @NotBlank
  @Size(max = 32)
  private String lastName;
  
  @Enumerated(EnumType.STRING)
  private AuthenticationMethod provider;
  
}
