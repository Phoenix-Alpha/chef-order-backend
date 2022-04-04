package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.PublicSearchChefResponse;
import com.halalhomemade.backend.models.Chef;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class ChefEntityToPublicSearchChefResponseMapper implements Function<Chef, PublicSearchChefResponse> {

	@Override
	public PublicSearchChefResponse apply(Chef chef) {
		return PublicSearchChefResponse.builder()
				.chefId(chef.getId())
				.chefProfileName(chef.getProfileName())
				.build();
	}
	
}
