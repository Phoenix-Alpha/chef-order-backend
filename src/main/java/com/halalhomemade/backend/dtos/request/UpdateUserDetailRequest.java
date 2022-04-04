package com.halalhomemade.backend.dtos.request;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
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
public class UpdateUserDetailRequest {
	@NotBlank
	@NotNull
	@Size(max = 254)
	private String email;
  
	@Size(max = 32)
	private String firstName;

	@Size(max = 32)
	private String lastName;
  
	@Size(max = 254)
	private String address;
  
	@Size(max = 16)
  	private String city;
	
	@Size(max = 16)
  	private String postCode;
  	
	private String phoneCode;
	
	private String phoneNumber;
	
	private String country;
	
  	private Instant birthdate;
  
  	private Long preferredLanguageId;
}
