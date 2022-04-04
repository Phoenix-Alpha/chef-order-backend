package com.halalhomemade.backend.dtos.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.constants.IValidationConstants;
import com.halalhomemade.backend.models.AuthenticationProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

  @NotBlank
  @Size(max = 254)
  @Email
  private String email;
  
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\^$*.\\[\\]{}\\(\\)?\\-“!@#%&\\/,><\\’:;|_~`])\\S{8,99}$", message = IValidationConstants.INVALID_PASSWORD_MESSAGE)
  private String password;
  
  @NotBlank
  @Size(max = 32)
  private String firstName;

  @NotBlank
  @Size(max = 32)
  private String lastName;
  
  private AuthenticationProvider social;
  
  private Long preferredLanguageId;
}
