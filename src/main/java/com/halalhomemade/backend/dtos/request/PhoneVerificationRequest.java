package com.halalhomemade.backend.dtos.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneVerificationRequest {

  @NotBlank
  @NotNull
  @Size(max = 254)
  private String email;
  
  @NotBlank
  @NotNull
  @Size(max = 6)
  private String phoneVerificationCode;
  
}
