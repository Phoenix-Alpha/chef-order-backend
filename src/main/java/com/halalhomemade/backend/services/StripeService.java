package com.halalhomemade.backend.services;

import org.springframework.stereotype.Service;

import com.halalhomemade.backend.dtos.request.ChefActivateWalletRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderCreateRequest;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.OfferType;
import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.models.Wallet;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.repositories.WalletRepository;
import com.halalhomemade.backend.utils.EncDecSymmetric;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.EphemeralKey;
import com.stripe.model.LoginLink;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountCreateParams.BusinessType;
import com.stripe.param.PaymentIntentCreateParams.CaptureMethod;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountUpdateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import static com.halalhomemade.backend.utils.EncDecSymmetric.keyToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeService {
	
	@Value("${stripe.apiKey}")
	private String stripeApiKey;

	@Value("${stripe.clientId}")
	private String stripeClientId;

	@Value("${stripe.webhookEndpointSecret}")
	private String stripeWebhookEndpointSecret;

	// @Value("${stripe.webhookConnectEndpointSecret}")
	// private String stripeConnectWebhookEndpointSecret;

	// @Value("${stripe.link}")
	// private String stripeLink;
	
	@Value("${api.url}")
	private String apiUrl;
	
	@Autowired ChefRepository chefRepository;
	@Autowired UserRepository userRepository;
	@Autowired WalletRepository walletRepository;
	
	@Autowired private EncDecSymmetric encDecSymmetric;
	
//	// Create Stripe connected account(custom) for chef. If already exists, retrieve and return it instead.
//	public Account createStripeConnectedAccount(Chef chef) {
//		Stripe.apiKey = stripeApiKey;
//		
//		try {
//			// assume that the customer country is always France, needs to be refactored later...
//			AccountCreateParams.Builder paramsBuilder = 
//				AccountCreateParams.builder()
//					.setCountry("FR")
//				    .setType(AccountCreateParams.Type.CUSTOM)
//				    .setEmail(chef.getUser().getEmail())
//				    .setBusinessType(BusinessType.INDIVIDUAL)
//				    .setIndividual(
//			    		AccountCreateParams.Individual.builder()
//			    			.setEmail(chef.getUser().getEmail())
//			    			.setFirstName(chef.getUser().getFirstName())
//				    		.setLastName(chef.getUser().getLastName())
//				    		.setPhone(chef.getUser().getPhoneCode() + chef.getUser().getPhoneNumber())
//				    		.setAddress(
//				    			AccountCreateParams.Individual.Address.builder()
//				    				.setCountry("FR")
//				    				.setCity(chef.getUser().getCity())
//				    				.setLine1(chef.getUser().getAddress())
//				    				.setPostalCode(chef.getUser().getPostCode())
//				    				.build())
//				    		.build())
//				    .setCapabilities(
//				    	AccountCreateParams.Capabilities.builder()
//				    		.setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
//				    			.setRequested(true)
//				    			.build())
//				    		.setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
//				    			.setRequested(true)
//				    			.build())
//				    		.build());
//			Account account = Account.create(paramsBuilder.build());
//			return account;
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return null;
//		}
//	}
//	
//	// Create Stripe connected account(custom) for chef. If already exists, retrieve and return it instead.
//	public Account activateChefStripeConnectedAccount(Chef chef, ChefActivateWalletRequest request) {
//		Stripe.apiKey = stripeApiKey;
//		
//		try {
//			// Check if the chef already has stripe account connected...
//			Optional<Wallet> chefWallet = walletRepository.findOneByUserId(chef.getUser().getId());
//			if (chefWallet.isPresent() && !chefWallet.get().getStripeAccountId().isEmpty()) {
//				Account existingAccount = Account.retrieve(chefWallet.get().getStripeAccountId());
//				
//				Map<String, Object> individualAddressParams = new HashMap<>();
//				individualAddressParams.put("country", "FR");
//				individualAddressParams.put("city", request.getBillingAddressCity());
//				individualAddressParams.put("line1", request.getBillingAddressLine());
//				individualAddressParams.put("postal_code", request.getBillingAddressPostcode());
//				
//				Map<String, Object> individualDobParams = new HashMap<>();
//				individualDobParams.put("day", Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getDayOfMonth()));
//				individualDobParams.put("month", Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getMonthValue()));
//				individualDobParams.put("year", Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getYear()));
//				
//				Map<String, Object> individualParams = new HashMap<>();
//				individualParams.put("dob", individualDobParams);
//				individualParams.put("address", individualAddressParams);
//				
//				Map<String, Object> businessProfileParams = new HashMap<>();
//				businessProfileParams.put("mcc", "5811");
//				businessProfileParams.put("product_description", "Selling Halal Homemade Foods");
//				
//				// Map<String, Object> tosParams = new HashMap<>();
//				// tosParams.put("date", Instant.now().getEpochSecond());
//				// tosParams.put("ip", request.getIp());
//				
//				Map<String, Object> updateParams = new HashMap<>();
//				updateParams.put("individual", individualParams);
//				updateParams.put("business_profile", businessProfileParams);
//				
//				// updateParams.put("tos_acceptance", tosParams);
//				
//				Account updatedAccount = existingAccount.update(updateParams);
//				return updatedAccount;
//			} else {
//				System.out.println("====================================================================================");
//				// assume that the customer country is always France, needs to be refactored later...
//				AccountCreateParams.Builder paramsBuilder = 
//					AccountCreateParams.builder()
//						.setCountry("FR")
//					    .setType(AccountCreateParams.Type.CUSTOM)
//					    .setEmail(chef.getUser().getEmail())
//					    .setBusinessType(BusinessType.INDIVIDUAL)
//					    .setIndividual(
//				    		AccountCreateParams.Individual.builder()
//				    			.setEmail(chef.getUser().getEmail())
//				    			.setFirstName(request.getLegalFirstName())
//					    		.setLastName(request.getLegalLastName())
//					    		.setPhone(chef.getUser().getPhoneCode() + chef.getUser().getPhoneNumber())
//					    		.setDob(
//				    				AccountCreateParams.Individual.Dob.builder()
//					    				.setYear(Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getYear()))
//					    				.setMonth(Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getMonthValue()))
//					    				.setDay(Long.valueOf(request.getBirthday().atOffset(ZoneOffset.UTC).getDayOfMonth()))
//					    				.build())
//					    		.setAddress(
//					    			AccountCreateParams.Individual.Address.builder()
//					    				.setCountry("FR")
//					    				.setCity(request.getBillingAddressCity())
//					    				.setLine1(request.getBillingAddressLine())
//					    				.setPostalCode(request.getBillingAddressPostcode())
//					    				.build())
//					    		.build())
//					    .setCapabilities(
//					    	AccountCreateParams.Capabilities.builder()
//					    		.setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
//					    			.setRequested(true)
//					    			.build())
//					    		.setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
//					    			.setRequested(true)
//					    			.build())
//					    		.build())
//					    .setBusinessProfile(
//					    	AccountCreateParams.BusinessProfile.builder()
//					    		.setMcc("5811")
//					    		.setProductDescription("Selling Halal Homemade Foods")
//					    		.build());
////					    .setTosAcceptance(
////					    	AccountCreateParams.TosAcceptance.builder()
////					    		.setDate(Instant.now().getEpochSecond())
////					    		.setIp(request.getIp())
////					    		.build());
//					    
//				Account account = Account.create(paramsBuilder.build());
//				return account;
//			}
//			
//			
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			return null;
//		}
//	}
	
	// Create Stripe connected account(custom) for chef. If already exists, retrieve and return it instead.
	public Account createStripeConnectedAccount(Chef chef) {
		Stripe.apiKey = stripeApiKey;
		
		try {
			// assume that the customer country is always France, needs to be refactored later...
			AccountCreateParams.Builder paramsBuilder = 
				AccountCreateParams.builder()
					.setCountry("FR")
				    .setType(AccountCreateParams.Type.EXPRESS)
				    .setEmail(chef.getUser().getEmail())
				    .setBusinessType(BusinessType.INDIVIDUAL)
				    .setIndividual(
			    		AccountCreateParams.Individual.builder()
			    			.setEmail(chef.getUser().getEmail())
			    			.setFirstName(chef.getUser().getFirstName())
				    		.setLastName(chef.getUser().getLastName())
				    		.setPhone(chef.getUser().getPhoneCode() + chef.getUser().getPhoneNumber())
				    		.setAddress(
				    			AccountCreateParams.Individual.Address.builder()
				    				.setCountry("FR")
				    				.setCity(chef.getUser().getCity())
				    				.setLine1(chef.getUser().getAddress())
				    				.setPostalCode(chef.getUser().getPostCode())
				    				.build())
				    		.build())
				    .setCapabilities(
				    	AccountCreateParams.Capabilities.builder()
				    		.setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
				    			.setRequested(true)
				    			.build())
				    		.setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
				    			.setRequested(true)
				    			.build())
				    		.build());
			Account account = Account.create(paramsBuilder.build());
			return account;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public AccountLink createConnectedAccountLinkFromAccount(Account account, AccountLinkCreateParams.Type type) {
		Stripe.apiKey = stripeApiKey;
		
		try {
			AccountLinkCreateParams linkParams =
				AccountLinkCreateParams.builder()
				    .setAccount(account.getId())
				    .setRefreshUrl(apiUrl + "/v1/stripe/redirect/chef/wallet/activate/refresh")
				    .setReturnUrl(apiUrl + "/v1/stripe/redirect/chef/wallet/activate/return")
				    .setType(type)
				    .setCollect(AccountLinkCreateParams.Collect.EVENTUALLY_DUE)
				    .build();
			AccountLink accountLink = AccountLink.create(linkParams);
			return accountLink;	
		} catch (StripeException se){
			System.out.println(se.toString());
			return null;
		}
	}
	
	public LoginLink createStripeExpressDashboardLink(Account account) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		return LoginLink.createOnAccount(account.getId(), (Map<String, Object>) null, (RequestOptions) null);	
	}
	
	public Customer retrieveStripeCustomerByEmail(String email) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		params.put("limit", 1);
		CustomerCollection customers = Customer.list(params);
		if (customers.getData().size() > 0) {
			return customers.getData().get(0);
		} else {
			return null;
		}
	}
	
	public Customer createStripeCustomer(Order order) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		
		CustomerCreateParams params = CustomerCreateParams.builder()
			.setEmail(order.getCustomerEmail())
			.setAddress(
				CustomerCreateParams.Address.builder()
					.setCountry("FR")
					.setCity(order.getDeliveryCity())
					.setLine1(order.getDeliveryStreetAddress())
					.setPostalCode(order.getDeliveryPostcode())
					.build())
			.setPhone(order.getCustomerPhoneNumber())
			.setName(order.getCustomerFirstName() + " " + order.getCustomerLastName())
			.build();
		Customer customer = Customer.create(params);
		return customer;
	}
	
	public PaymentMethod attachPaymentMethodToCustomer(PaymentMethod paymentMethod, Customer customer) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		
		Map<String, Object> params = new HashMap<>();
		params.put("customer", customer.getId());
		PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);
		return updatedPaymentMethod;
	}
	
	public Session createStripeCheckoutSession(Order order) {
		Stripe.apiKey = stripeApiKey;
		
		try {
			SessionCreateParams.Builder paramsBuilder =
		        SessionCreateParams.builder()
		        	.setCustomer(order.getStripeCustomerId())
		        	.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
		        	.setMode(SessionCreateParams.Mode.PAYMENT)
		        	.setSuccessUrl(apiUrl + "/v1/stripe/redirect/order/checkout/success?session_id={CHECKOUT_SESSION_ID}")
		        	.setCancelUrl(apiUrl + "/v1/stripe/redirect/order/checkout/failure?session_id={CHECKOUT_SESSION_ID}\"")
		        	.addLineItem(
		        		SessionCreateParams.LineItem.builder()
		        			.setQuantity(order.getQuantity().longValue())
		        			.setPriceData(
		        				SessionCreateParams.LineItem.PriceData.builder()
		        					.setCurrency("eur")
		        					.setUnitAmount(order.getOffer().getPrice().longValue() * 100)
		        					.setProductData(
		        						SessionCreateParams.LineItem.PriceData.ProductData.builder()
		        							.setName(order.getOffer().getTitle())
		        							.setDescription(order.getOffer().getDescription())
		        							.build())
		        					.build())
	        				.build())
		        	.setClientReferenceId(keyToString(encDecSymmetric.encryptText(order.getUuid() + ";" + order.getOrderNumber())));
			
			if (order.getOffer().getOfferType().equals(OfferType.PREORDER)) {
				paramsBuilder.setPaymentIntentData(
        			SessionCreateParams.PaymentIntentData.builder()
        				.setSetupFutureUsage(SessionCreateParams.PaymentIntentData.SetupFutureUsage.ON_SESSION)
        	        	.build());
			} else if (order.getOffer().getOfferType().equals(OfferType.ONDEMAND)) {
				paramsBuilder.setPaymentIntentData(
        			SessionCreateParams.PaymentIntentData.builder()
        				.setCaptureMethod(SessionCreateParams.PaymentIntentData.CaptureMethod.MANUAL)
        	        	.setSetupFutureUsage(SessionCreateParams.PaymentIntentData.SetupFutureUsage.ON_SESSION)
        	        	.build());
			} else {
				throw new Exception("Unsupported offer type");
			}
			
			Session session = Session.create(paramsBuilder.build());
		    return session;  
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public EphemeralKey createStripeEphemeralKey(Customer customer) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		
		EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
			.setCustomer(customer.getId())
			.build();
	
		RequestOptions ephemeralKeyOptions = RequestOptions.builder()
	    	.setStripeVersionOverride("2020-08-27")
		    .build();
	
		EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams, ephemeralKeyOptions);
		
		return ephemeralKey;
	}
	
	
	public PaymentIntent createStripePaymentIntent(Order order, Customer customer) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		
		PaymentIntentCreateParams.Builder paymentIntentParamsBuilder = PaymentIntentCreateParams.builder()
			.setAmount(order.getTotalDiscountedCost().multiply(BigDecimal.valueOf(100)).longValue())
			.setDescription(order.getOffer().getTitle() + " x " + order.getQuantity())
			.setCurrency("eur")
			.setCustomer(customer.getId());
			

		if (order.getOffer().getOfferType().equals(OfferType.PREORDER)) {
			paymentIntentParamsBuilder.setCaptureMethod(CaptureMethod.AUTOMATIC);
		} else if (order.getOffer().getOfferType().equals(OfferType.ONDEMAND)) {
			paymentIntentParamsBuilder.setCaptureMethod(CaptureMethod.MANUAL);
		}
		
		PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParamsBuilder.build());
		
		return paymentIntent;
	}
	
	
	public Account retrieveStripeConnectedAccountById(String stripeAccountId) throws StripeException {
		Stripe.apiKey = stripeApiKey;
		Account account = Account.retrieve(stripeAccountId);
		return account;
	}
	
}