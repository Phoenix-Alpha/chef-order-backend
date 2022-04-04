package com.halalhomemade.backend.controllers;

import com.google.gson.JsonSyntaxException;
import com.halalhomemade.backend.models.PaymentStatus;
import com.halalhomemade.backend.models.WalletStatus;
import com.halalhomemade.backend.services.ChefService;
import com.halalhomemade.backend.services.PublicOrderService;
import com.halalhomemade.backend.utils.EncDecSymmetric;
import static com.halalhomemade.backend.utils.EncDecSymmetric.stringToKey;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Account;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stripe/webhook")
@Validated
public class StripeWebhookController extends BaseController {

	@Value("${stripe.webhookEndpointSecret}")
	private String stripeWebhookEndpointSecret;

	// @Value("${stripe.webhookConnectEndpointSecret}")
	// private String stripeConnectWebhookEndpointSecret;

	@Value("${stripe.apiKey}")
	private String stripeApiKey;
	
	@Autowired private ChefService chefService;
	@Autowired private PublicOrderService publicOrderService;
	
	@Autowired private EncDecSymmetric encDecSymmetric;
	
	@PostMapping(value = "/checkout-session")
	public ResponseEntity handleStripeCheckoutSessionEvent(@RequestHeader("Stripe-Signature") String signature, @RequestBody String payload) {
		System.out.println(signature);
		Stripe.apiKey = stripeApiKey;
		Event event = null;
	    try {
	    	event = Webhook.constructEvent(payload, signature, stripeWebhookEndpointSecret);
	    } catch (JsonSyntaxException e) {
	    	// Invalid payload
	    	return ResponseEntity.badRequest().build();
	    } catch (SignatureVerificationException e) {
	        // Invalid signature
	    	return ResponseEntity.badRequest().build();
	    }
	    
	    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
	    
	    if (!dataObjectDeserializer.getObject().isPresent()) {
	    	return ResponseEntity.badRequest().build();
	    }
	    
	    switch (event.getType()) {
      		case "checkout.session.completed":
      			try {
      				Session session = (Session) dataObjectDeserializer.getObject().get();
          			
          			System.out.println("checkout.session.completed");
          			System.out.println(session.getPaymentStatus());
          			
          			// Retrieve order uuid from the checkout session.
          			String orderUuid = encDecSymmetric.decryptText(stringToKey(session.getClientReferenceId()));
          			
          			System.out.println(orderUuid);
          			
          			// Check if the order is paid (e.g., from a card payment)
          			if (session.getPaymentStatus() == "paid") {
          				publicOrderService.updateOrderPaymentStatus(orderUuid, PaymentStatus.SUCCESSFULLY_COMPLETED);
          				return ResponseEntity.ok().build();
          			} else if (session.getPaymentStatus() == "unpaid") {
          				publicOrderService.updateOrderPaymentStatus(orderUuid, PaymentStatus.FAILED);
          				return ResponseEntity.ok().build();
          			} else if (session.getPaymentStatus() == "no_payment_required") {
          				publicOrderService.updateOrderPaymentStatus(orderUuid, PaymentStatus.SUCCESSFULLY_COMPLETED);
          				return ResponseEntity.ok().build();
          			} else {
          				// 
          				return ResponseEntity.badRequest().build();
          			}
      			} catch (Exception e) {
      				System.out.println(e.toString());
      				return ResponseEntity.badRequest().build();
      			}
      		case "checkout.session.async_payment_succeeded":
      			System.out.print("checkout.session.async_payment_succeeded");

      			break;
      		case "checkout.session.async_payment_failed":
      			System.out.print("checkout.session.async_payment_failed");

      			break;
	    }
	      		
	    return ResponseEntity.ok().build();
	}
	
	@PostMapping(
			value = "/connect",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity handleStripeConnectEvent(@RequestHeader("Stripe-Signature") String signature, @RequestBody String payload) {
		Stripe.apiKey = stripeApiKey;
		Event event = null;
	    try {
	    	event = Webhook.constructEvent(payload, signature, stripeWebhookEndpointSecret);
	    } catch (JsonSyntaxException e) {
	    	// Invalid payload
	    	return ResponseEntity.badRequest().build();
	    } catch (SignatureVerificationException e) {
	        // Invalid signature
	    	return ResponseEntity.badRequest().build();
	    }
	    
	    if ("account.updated".equals(event.getType())) {
	    	
	    	try {
	    		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
			    
			    if (!dataObjectDeserializer.getObject().isPresent()) {
			    	return ResponseEntity.badRequest().build();
			    }
			    
			    Account account = (Account) dataObjectDeserializer.getObject().get();
			    
			    if (account.getPayoutsEnabled()) {
			    	chefService.updateWalletStatus(account, WalletStatus.ENABLED);
			    }
		    	
			    System.out.println(account);
	
	    	} catch (Exception e) {
	    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    	}
	    		    	
	    	return ResponseEntity.ok().build();
	    }
	    
	    return ResponseEntity.ok().build();
	}
	
	
}
