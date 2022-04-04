package com.halalhomemade.backend.services;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.halalhomemade.backend.dtos.request.*;
import com.halalhomemade.backend.dtos.response.ChefOfferResponse;
import com.halalhomemade.backend.dtos.response.UploadOfferPictureResponse;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.mappers.ChefOfferCreateRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.OfferEntityToDtoMapper;
import com.halalhomemade.backend.services.mappers.UpdateChefOfferDetailRequestToEntityMapper;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.Role;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserRole;
import com.halalhomemade.backend.models.VerificationStatus;
import com.halalhomemade.backend.exceptions.DuplicateChefException;
import com.halalhomemade.backend.exceptions.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class ChefOfferService extends AbstractService {

	@Autowired private AwsService awsService;
	
	@Autowired private UserRepository userRepository;
	@Autowired private ChefRepository chefRepository;
	
	@Autowired private OfferRepository offerRepository;
	@Autowired private ChefOfferCreateRequestToEntityMapper chefOfferCreateRequestToEntityMapper;
	@Autowired private UpdateChefOfferDetailRequestToEntityMapper updateChefOfferDetailRequestToEntityMapper;
	@Autowired private OfferEntityToDtoMapper offerEntityToDtoMapper;
	
  	public ResponseEntity<ChefOfferResponse> create(ChefOfferCreateRequest request) {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
  			if (!existingUser.isPresent()) {
  	  			throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
  	  		}
  			User user = existingUser.get();
  			
  			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  	  		if (!existingChef.isPresent()) {
  	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
  	  		}
  	  		Chef chef = existingChef.get();
  	  		
  	  		Optional<Offer> newOffer = Optional.ofNullable(chefOfferCreateRequestToEntityMapper.apply(request));
  	  		if (newOffer.isPresent()) {
  	  			Offer savedOffer = offerRepository.save(newOffer.get());
  	  			// Update chef's active offers by increasing 1
  	  			chef.setActiveOffers(chef.getActiveOffers() + 1);
  	  			Chef savedChef = chefRepository.save(chef);
  	  			
	  	  		return new ResponseEntity<>(offerEntityToDtoMapper.apply(savedOffer), HttpStatus.OK);
  	  		} else {
  	  			throw new Exception("Invaild arguments");
  	  		}
  		} catch (Exception e) {
  			log.error(e.toString());
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
  	}
  
  	public ResponseEntity<ChefOfferResponse> getChefOfferDetail(ChefOfferDetailFetchRequest request) {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
  			if (!existingUser.isPresent()) {
  	  			throw new Exception(String.format("Chef not existing with email: %s", request.getEmail()));
  	  		}
  			User user = existingUser.get();
  			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  			if (!existingChef.isPresent()) {
  				throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
  			}
  			
  			Optional<Offer> existingOffer = offerRepository.findOneById(request.getOfferId());
  			if (existingOffer.isPresent()) {
  				Offer offer = existingOffer.get();
  				if (offer.getChef().getId() != existingChef.get().getId()) {
  					throw new Exception("Invalid request");
  				}	
  			} else {
  				throw new Exception("Offer not exist");
  			}
  			return new ResponseEntity<>(offerEntityToDtoMapper.apply(existingOffer.get()), HttpStatus.OK);
  		} catch (Exception e) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}
  	
  	public ResponseEntity<ChefOfferResponse> updateChefOfferDetail(UpdateChefOfferDetailRequest request) {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
  			if (!existingUser.isPresent()) {
  	  			throw new Exception(String.format("Chef not existing with email: %s", request.getEmail()));
  	  		}
  			User user = existingUser.get();
  			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  			if (!existingChef.isPresent()) {
  				throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
  			}
  			Chef chef = existingChef.get();
  			
  			Optional<Offer> existingOffer = offerRepository.findOneById(request.getOfferId());
  			if (!existingOffer.isPresent()) {
  				throw new Exception("Offer not exist");
  					
  			}
  			Offer offer = existingOffer.get();
  			
  			// Check if the offer belongs to the chef
			if (offer.getChef().getId() != chef.getId()) {
				throw new Exception("Invalid request");
			}
			
  			// Update chef status if status is updated
  			if (!offer.getStatus().equals(request.getStatus())) {
  				if (request.getStatus().equals(OfferStatus.ACTIVE)) {
  					chef.setActiveOffers(chef.getActiveOffers() + 1);
  				} else if (request.getStatus().equals(OfferStatus.ARCHIVE)) {
  					chef.setActiveOffers(chef.getActiveOffers() - 1);
  				}
  			}
  			
  			// Update offer and save
  			Offer savedOffer = offerRepository.save(updateChefOfferDetailRequestToEntityMapper.apply(request));
  			
  			// Save updated chef status
  			Chef savedChef = chefRepository.save(chef);
  			
  			return new ResponseEntity<>(offerEntityToDtoMapper.apply(savedOffer), HttpStatus.OK);
  			
  		} catch (Exception e) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}
  	
  	
  	public ResponseEntity<List<ChefOfferResponse>> getOffersByStatus(FetchOffersByStatusRequest request) {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
  			if (!existingUser.isPresent()) {
  	  			throw new Exception(String.format("Chef not existing with email: %s", request.getEmail()));
  	  		}
  			User user = existingUser.get();
  			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  			if (!existingChef.isPresent()) {
  				throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
  			}
  			Chef chef = existingChef.get();
  			List<Offer> offers = offerRepository.findAllByChefAndStatus(chef, request.getStatus());
  			
  			List<ChefOfferResponse> offersResponse = new ArrayList<ChefOfferResponse>();
  			
  			offers.forEach(offer -> {
  				offersResponse.add(offerEntityToDtoMapper.apply(offer));
  			});
  			
  			return new ResponseEntity<>(offersResponse, HttpStatus.OK);
  			
  		} catch (Exception e) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}

  	public UploadOfferPictureResponse uploadOfferPicture(String email, MultipartFile file) { 
  		try {
        	byte[] bytes = file.getBytes();
        	
        	Instant instant = Instant.now();
        	long timeStampMillis = instant.toEpochMilli();
        	
        	String hashedUrl = Hashing.sha256()
        			.hashString(email, StandardCharsets.UTF_8)
        			.toString();
        	
        	String hashedFileName = Hashing.sha256()
        			.hashLong(timeStampMillis)
        			.toString();
        	
        	String objectKey = "offers/" + hashedUrl + "/" + hashedFileName + "." + Files.getFileExtension(file.getOriginalFilename());
            String avatarURL = awsService.putObject(bytes, objectKey);
            return UploadOfferPictureResponse.builder()
            		.success(true)
            		.url(avatarURL)
            		.build();
        } catch (IOException e) {
        	return UploadOfferPictureResponse.builder()
            		.success(false)
            		.url("")
            		.build();
        }
  	}
}
