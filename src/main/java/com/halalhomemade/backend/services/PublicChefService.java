package com.halalhomemade.backend.services;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.maps.model.GeocodingResult;
import com.halalhomemade.backend.dtos.request.*;
import com.halalhomemade.backend.dtos.response.ChefResponse;
import com.halalhomemade.backend.dtos.response.PublicChefResponse;
import com.halalhomemade.backend.dtos.response.PublicSearchChefResponse;
import com.halalhomemade.backend.dtos.response.UploadAvatarResponse;
import com.halalhomemade.backend.dtos.response.UserResponse;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.repositories.RoleUserRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.mappers.ChefRegistrationRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.PublicFetchChefDetailRequestToPublicChefResponseMapper;
import com.halalhomemade.backend.services.mappers.UpdateChefDetailRequestToEntityMapper;
import com.halalhomemade.backend.services.events.ChefRegistrationCompleteEvent;
import com.halalhomemade.backend.services.mappers.ChefEntityToDtoMapper;
import com.halalhomemade.backend.services.mappers.ChefEntityToPublicSearchChefResponseMapper;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.Position;
import com.halalhomemade.backend.models.RoleUser;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserRole;
import com.halalhomemade.backend.models.Role;
import com.halalhomemade.backend.models.VerificationStatus;
import com.halalhomemade.backend.exceptions.DuplicateChefException;
import com.halalhomemade.backend.exceptions.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PublicChefService extends AbstractService {

	@Autowired private ChefRepository chefRepository;
	
	@Autowired private GoogleGeoApiService googleGeoApiService;
	
	@Autowired private PublicFetchChefDetailRequestToPublicChefResponseMapper publicFetchChefDetailRequestToPublicChefResponseMapper;
	@Autowired private ChefEntityToPublicSearchChefResponseMapper chefEntityToPublicSearchChefResponseMapper;
  
	public ResponseEntity<List<PublicSearchChefResponse>> getChefListFromName(String chefName) {
		List<Chef> chefList = chefRepository.findByProfileNameLike("%" + chefName + "%");
		return new ResponseEntity<>(chefList.stream().map(chefEntityToPublicSearchChefResponseMapper).collect(Collectors.toList()), HttpStatus.OK);
	}
	
	
  	public ResponseEntity<PublicChefResponse> getChefDetailWithOfferList(PublicFetchChefDetailRequest request) {
  		Optional<Chef> existingChef = chefRepository.findById(request.getChefId());
		if (!existingChef.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			
			Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(request.getUserAddress()));
		    BigDecimal userLat = null;
		    BigDecimal userLon = null;
			if (geoResult.isPresent()) {
				userLat = geoResult.get().getLatitude();
				userLon = geoResult.get().getLongitude();
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			request.setLatitude(userLat);
			request.setLongitude(userLon);
			
			return new ResponseEntity<>(publicFetchChefDetailRequestToPublicChefResponseMapper.apply(request), HttpStatus.OK);
		}
	}
    
}
