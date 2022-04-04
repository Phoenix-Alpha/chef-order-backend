package com.halalhomemade.backend.dtos.request;

import com.halalhomemade.backend.models.AuthenticationProvider;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class SocialRegistration {
//
//  @NotBlank private String accessToken;
//
//  @NotNull private AuthenticationProvider provider;
//}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialRegistration {
	
	@NotBlank
    private String email;

    @NotNull
    private AuthenticationProvider provider;

    @NotBlank
	private String firstName;

    @NotBlank
	private String lastName;

	private String picture;
}
