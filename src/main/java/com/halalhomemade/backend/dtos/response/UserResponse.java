package com.halalhomemade.backend.dtos.response;

import com.halalhomemade.backend.models.AuthenticationMethod;
import com.halalhomemade.backend.models.UserRole;
import com.halalhomemade.backend.models.VerificationStatus;
import java.time.Instant;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class UserResponse {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneCode;
  private String phoneNumber;
  private VerificationStatus phoneVerified;
  private AuthenticationMethod authenticationMethod;
  private LanguageResponse preferredLanguage;
//  private UserRole userRole;
  private Set<UserRole> roles;
  private Instant registeredAt;
  private Boolean supperAdmin;
  private Boolean isChef;
  private String address;
  private String city;
  private String postCode;
  private String country;
  private Instant birthdate;
}