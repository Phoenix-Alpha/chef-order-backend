package com.halalhomemade.backend.services;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.halalhomemade.backend.dtos.request.*;
import com.halalhomemade.backend.dtos.response.ChefResponse;
import com.halalhomemade.backend.dtos.response.ChefWalletActivateResponse;
import com.halalhomemade.backend.dtos.response.ChefWalletStripeDashboardLoginResponse;
import com.halalhomemade.backend.dtos.response.UploadAvatarResponse;
import com.halalhomemade.backend.dtos.response.WalletResponse;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.repositories.RoleUserRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.repositories.WalletRepository;
import com.halalhomemade.backend.services.mappers.ChefRegistrationRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.UpdateChefDetailRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.WalletEntityToWalletResponseMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.LoginLink;
import com.stripe.param.AccountLinkCreateParams;
import com.halalhomemade.backend.services.mappers.ChefEntityToDtoMapper;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserRole;
import com.halalhomemade.backend.models.Role;
import com.halalhomemade.backend.models.Wallet;
import com.halalhomemade.backend.models.WalletStatus;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ChefService extends AbstractService {

	@Autowired private AwsService awsService;
	@Autowired private StripeService stripeService;
	@Autowired private WalletService walletService;
	
	@Autowired private UserRepository userRepository;
	@Autowired private RoleUserRepository roleUserRepository;
	@Autowired private RoleRepository roleRepository;
	@Autowired private ChefRepository chefRepository;
	@Autowired private WalletRepository walletRepository;
	
	@Autowired private ChefRegistrationRequestToEntityMapper chefRegistrationRequestToEntityMapper;
	@Autowired private UpdateChefDetailRequestToEntityMapper updateChefDetailRequestToEntityMapper;
	@Autowired private ChefEntityToDtoMapper chefEntityToDtoMapper;
	@Autowired WalletEntityToWalletResponseMapper walletEntityToWalletResponseMapper;
	// @Autowired private ApplicationEventPublisher eventPublisher;
  
  	public ResponseEntity<ChefResponse> register(ChefRegistrationRequest request) {
  		Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
		if (!existingUser.isPresent()) {
  			// throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
		User user = existingUser.get();
		
		Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  		if (existingChef.isPresent()) {
  			// throw new DuplicateChefException(String.format("Chef is already registered with email: %s", request.getEmail()));
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
  		Chef newChef = chefRegistrationRequestToEntityMapper.apply(request);
		
  		// create connected stripe account on chef register...
		Optional<Account> existingAccount = Optional.ofNullable(stripeService.createStripeConnectedAccount(newChef));
		if (!existingAccount.isPresent()) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Account stripeAccount = existingAccount.get();
		
		// create chef's wallet and connect stripe account
		walletService.connectStripeAccountToChefWallet(newChef, stripeAccount);
		
		// save chef
		Chef savedChef = chefRepository.save(newChef);
		
		// update user role with chef
		Optional<Role> role = roleRepository.findOneByName(UserRole.CHEF);
		if (role.isPresent()) {
			user.addRole(role.get());
		}
		User savedUser = userRepository.save(user);
		
		return new ResponseEntity<ChefResponse>(chefEntityToDtoMapper.apply(savedChef), HttpStatus.OK);
  	}
  
  	public ResponseEntity<ChefResponse> getChefDetail(String email) {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(email);
  			if (!existingUser.isPresent()) {
  	  			throw new Exception(String.format("Chef not existing with email: %s", email));
  	  		}
  			User user = existingUser.get();
  			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
  			if (!existingChef.isPresent()) {
  				throw new Exception(String.format("Chef not exsting with email: %s", email));
  			}
  			return new ResponseEntity<ChefResponse>(chefEntityToDtoMapper.apply(existingChef.get()), HttpStatus.OK);  			
  		} catch (Exception e) {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
	}

  	public UploadAvatarResponse uploadChefAvatar(String email, MultipartFile file) { 
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
        	
        	String objectKey = "avatars/chef/" + hashedUrl + "/" + hashedFileName + "." + Files.getFileExtension(file.getOriginalFilename());
            String avatarURL = awsService.putObject(bytes, objectKey);
            return UploadAvatarResponse.builder()
            		.success(true)
            		.url(avatarURL)
            		.build();
        } catch (IOException e) {
        	return UploadAvatarResponse.builder()
            		.success(false)
            		.url("")
            		.build();
        }
  	}
  	
  	public UploadAvatarResponse updateChefAvatar(String email, MultipartFile file)  {
  		try {
  			Optional<User> existingUser = userRepository.findOneByEmail(email);
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s", email));
	  		}
			User user = existingUser.get();
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", email));
	  		}
  		
        	byte[] bytes = file.getBytes();
        	
        	Instant instant = Instant.now();
        	long timeStampMillis = instant.toEpochMilli();
        	
        	String hashedUrl = Hashing.sha256()
        			.hashString(email, StandardCharsets.UTF_8)
        			.toString();
        	
        	String hashedFileName = Hashing.sha256()
        			.hashLong(timeStampMillis)
        			.toString();
        	
        	String objectKey = "avatars/chef/" + hashedUrl + "/" + hashedFileName + "." + Files.getFileExtension(file.getOriginalFilename());
            String avatarURL = awsService.putObject(bytes, objectKey);
            
            Chef chef = existingChef.get();
            chef.setProfilePicture(avatarURL);
            chefRepository.save(chef);
            
            return UploadAvatarResponse.builder()
            		.success(true)
            		.url(avatarURL)
            		.build();
        } catch (Exception e) {
        	return UploadAvatarResponse.builder()
            		.success(false)
            		.url("")
            		.build();
        }
  	}
  	
	public ResponseEntity<ChefResponse> updateChefDetail(UpdateChefDetailRequest request) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s", request.getEmail()));
	  		}
			User user = existingUser.get();
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));
	  		}
			Chef savedChef = chefRepository.save(updateChefDetailRequestToEntityMapper.apply(request));
			return new ResponseEntity<ChefResponse>(chefEntityToDtoMapper.apply(savedChef), HttpStatus.OK);	
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
//	public ResponseEntity<List<ChefPaymentMethodResponse>> addCardToWallet(ChefWalletAddCardRequest request) {
//		try {
//			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
//			if (!existingUser.isPresent()) {
//	  			throw new Exception(String.format("Chef not existing with email: %s", request.getEmail()));
//	  		}
//			User user = existingUser.get();
//			
//			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
//			if (!existingChef.isPresent()) {
//				throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
//			}
//			Chef chef = existingChef.get();
//			
//			// Retrieves Stripe connected account
//			Optional<Account> existingAccount = Optional.ofNullable(stripeService.createOrRetrieveStripeConnectedAccount(chef));
//			if (!existingAccount.isPresent()) {
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			Account account = existingAccount.get();
//			
//			Map<String, Object> cardMap = new HashMap<>();
//			cardMap.put("object", "card");
//			cardMap.put("number", "FR");
//			cardMap.put("exp_month", "eur");
//			cardMap.put("exp_year", user.getFirstName() + " " + user.getLastName());
//			cardMap.put("cvc", "individual");
//			cardMap.put("currency", "eur");
//			cardMap.put("name", request.getFirstName() + " " + request.getLastName());
//			Map<String, Object> params = new HashMap<>();
//			params.put("external_account", cardMap);
//			Card card = (Card) account.getExternalAccounts().create(params);
//			
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//		
//	}

//	public ResponseEntity<ChefWalletActivateResponse> activateWallet(ChefActivateWalletRequest request) {
//		try {
//			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
//			if (!existingUser.isPresent()) {
//	  			throw new Exception(String.format("Chef not existing with email: %s", request.getEmail()));
//	  		}
//			User user = existingUser.get();
//			
//			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
//			if (!existingChef.isPresent()) {
//				throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));  
//			}
//			Chef chef = existingChef.get();
//			
//			// Retrieves Stripe connected account
//			Optional<Account> existingAccount = Optional.ofNullable(stripeService.activateChefStripeConnectedAccount(chef, request));
//			if (!existingAccount.isPresent()) {
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			Account account = existingAccount.get();
//			
//			Optional<Wallet> chefWallet = walletRepository.findOneByUserId(chef.getUser().getId());
//			if (!chefWallet.isPresent()) {
//				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			Wallet wallet = chefWallet.get();
//			
//			System.out.println(request.getAccountNumber());
//			
//			wallet.setStripeAccountId(account.getId());
//			
//			WalletResponse walletResponse = walletEntityToWalletResponseMapper.apply(wallet);
//			
//			// Creates external account for the chef to enable payouts
//			Map<String, Object> bankAccountMap = new HashMap<>();
//			bankAccountMap.put("object", "bank_account");
//			bankAccountMap.put("country", "FR");
//			bankAccountMap.put("currency", "eur");
//			bankAccountMap.put("account_holder_name", request.getLegalFirstName() + " " + request.getLegalLastName());
//			bankAccountMap.put("account_holder_type", "individual");
//			bankAccountMap.put("account_number", request.getAccountNumber()); // IBAN
//			Map<String, Object> params = new HashMap<>();
//			params.put("external_account", bankAccountMap);
//			BankAccount bankAccount = (BankAccount) account.getExternalAccounts().create(params);
//			
//			Optional<AccountLink> existingAccountLink = Optional.ofNullable(stripeService.createConnectedAccountLinkFromAccount(account, AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING));
//			if (!existingAccountLink.isPresent()) {
//			    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//			AccountLink accountLink = existingAccountLink.get();
//			ChefWalletActivateResponse response = ChefWalletActivateResponse.builder()
//				 .walletResponse(walletResponse)
//				 .accountLinkUrl(accountLink.getUrl())
//				 .expiresAt(accountLink.getExpiresAt())
//				 .build();
//			
//			Wallet savedWallet = walletRepository.save(wallet);
//			
//			return new ResponseEntity<ChefWalletActivateResponse>(response, HttpStatus.OK);
//			
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}
	
	public ResponseEntity<ChefWalletActivateResponse> activateWallet(String email) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(email);
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("Chef not existing with email: %s", email));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
			if (!existingChef.isPresent()) {
				throw new Exception(String.format("Chef not exsting with email: %s", email));  
			}
			Chef chef = existingChef.get();
			
			Optional<Wallet> chefWallet = walletRepository.findOneByUserId(chef.getUser().getId());
			if (!chefWallet.isPresent()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Wallet wallet = chefWallet.get();
			
			Optional<String> existingStripeAccountId = Optional.ofNullable(wallet.getStripeAccountId());
			String stripeAccountId = "";
			if (!existingStripeAccountId.isPresent() || existingStripeAccountId.get().isEmpty()) {
				
				// if wallet has no connected stripe account, create one now!
				
				// create connected stripe account on chef register...
				Optional<Account> existingAccount = Optional.ofNullable(stripeService.createStripeConnectedAccount(chef));
				if (!existingAccount.isPresent()) {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				Account stripeAccount = existingAccount.get();
				
				// create chef's wallet and connect stripe account
				walletService.connectStripeAccountToChefWallet(chef, stripeAccount);
				
				// save chef
				chef = chefRepository.save(chef);
				
				stripeAccountId = stripeAccount.getId();
			} else {
				stripeAccountId = existingStripeAccountId.get();
			}
			
			System.out.println(stripeAccountId);
			Account account = null;
			try {
				account = stripeService.retrieveStripeConnectedAccountById(stripeAccountId);	
			} catch (StripeException se) {
				System.out.println(se.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			WalletResponse walletResponse = walletEntityToWalletResponseMapper.apply(wallet);
			
			Optional<AccountLink> existingAccountLink = Optional.ofNullable(stripeService.createConnectedAccountLinkFromAccount(account, AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING));
			if (!existingAccountLink.isPresent()) {
			    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			AccountLink accountLink = existingAccountLink.get();
			ChefWalletActivateResponse response = ChefWalletActivateResponse.builder()
				 .walletResponse(walletResponse)
				 .accountLinkUrl(accountLink.getUrl())
				 .expiresAt(accountLink.getExpiresAt())
				 .build();
			
			Wallet savedWallet = walletRepository.save(wallet);
			
			return new ResponseEntity<ChefWalletActivateResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<ChefWalletStripeDashboardLoginResponse> redirectToStripeExpressDashboard(String email) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(email);
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("Chef not existing with email: %s", email));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
			if (!existingChef.isPresent()) {
				throw new Exception(String.format("Chef not exsting with email: %s", email));  
			}
			Chef chef = existingChef.get();
			
			Optional<Wallet> chefWallet = walletRepository.findOneByUserId(chef.getUser().getId());
			if (!chefWallet.isPresent()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Wallet wallet = chefWallet.get();
			
			Optional<String> existingStripeAccountId = Optional.ofNullable(wallet.getStripeAccountId());
			if (!existingStripeAccountId.isPresent()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			Account account = null;
			try {
				account = stripeService.retrieveStripeConnectedAccountById(existingStripeAccountId.get());	
			} catch (StripeException se) {
				System.out.println(se.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			LoginLink loginLink = null;
			try {
				loginLink = stripeService.createStripeExpressDashboardLink(account);
			} catch (StripeException se) {
				System.out.println(se.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			ChefWalletStripeDashboardLoginResponse response = ChefWalletStripeDashboardLoginResponse.builder()
					.loginLinkUrl(loginLink.getUrl())
					.build();
			return new ResponseEntity<ChefWalletStripeDashboardLoginResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<WalletResponse> getWalletDetail(String email) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(email);
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("Chef not existing with email: %s", email));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
			if (!existingChef.isPresent()) {
				throw new Exception(String.format("Chef not exsting with email: %s", email));  
			}
			Chef chef = existingChef.get();
			
			Optional<Wallet> chefWallet = walletRepository.findOneByUserId(chef.getUser().getId());
			if (!chefWallet.isPresent()) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Wallet wallet = chefWallet.get();
			
			System.out.println(wallet.getStripeAccountId());
			try {
				// Retrieves Stripe connected account
				Account account = stripeService.retrieveStripeConnectedAccountById(wallet.getStripeAccountId());
				
				if (account.getPayoutsEnabled()) {
					wallet.setStatus(WalletStatus.ENABLED);
					wallet = walletRepository.save(wallet);
				}
			} catch (StripeException se) {
				System.out.println(se.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			WalletResponse walletResponse = walletEntityToWalletResponseMapper.apply(wallet);
			
			return new ResponseEntity<WalletResponse>(walletResponse, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public void updateWalletStatus(Account account, WalletStatus newStatus) throws Exception {
		Optional<Wallet> chefWallet = walletRepository.findOneByStripeAccountId(account.getId());
		if (!chefWallet.isPresent()) {
			throw new Exception("Account not existing. Invalid request.");
		}
		Wallet wallet = chefWallet.get();
		wallet.setStatus(newStatus);
		walletRepository.save(wallet);
	}
}
