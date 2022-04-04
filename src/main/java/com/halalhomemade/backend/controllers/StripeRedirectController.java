package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.services.ChefService;
import com.halalhomemade.backend.services.PublicOrderService;
import com.halalhomemade.backend.utils.EncDecSymmetric;
import static com.halalhomemade.backend.utils.EncDecSymmetric.stringToKey;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/stripe/redirect")
@Validated
public class StripeRedirectController extends BaseController {

	@Value("${stripe.webhookEndpointSecret}")
	private String stripeWebhookEndpointSecret;

	// @Value("${stripe.webhookConnectEndpointSecret}")
	// private String stripeConnectWebhookEndpointSecret;

	@Value("${stripe.apiKey}")
	private String stripeApiKey;
	
	@Autowired private ChefService chefService;
	@Autowired private PublicOrderService publicOrderService;
	
	@Autowired private EncDecSymmetric encDecSymmetric;
	
	@GetMapping(value = "/chef/wallet/activate/refresh")
	public RedirectView chefActivateWalletRefresh() {
		RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/chef/wallet/refresh");
	    rv.setStatusCode(HttpStatus.FOUND);
	    return rv;
	}
	
	@GetMapping(value = "/chef/wallet/activate/return")
	public RedirectView chefActivateWalletReturn() {
		RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/chef/wallet/return");
	    rv.setStatusCode(HttpStatus.FOUND);
	    return rv;
	}
	
	@GetMapping(value = "/order/checkout/success")
	public RedirectView publicOrderCheckoutSuccess(@RequestParam(value="session_id", required = true) String sessionId) {
		try {
			Session session = Session.retrieve(sessionId);
			String paymentStatus = session.getPaymentStatus();
			String decryptedToken = encDecSymmetric.decryptText(stringToKey(session.getClientReferenceId()));
			String[] params = decryptedToken.split(";");
			String orderUuid = params[0];
			String orderNumber = params[1];  
			RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/checkout/success?orderUuid=" + orderUuid + "&paymentStatus=" + paymentStatus + "&orderNumber=" + orderNumber);
		    rv.setStatusCode(HttpStatus.FOUND);
		    return rv;	
		} catch (StripeException se) {
			RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/checkout/success");
		    rv.setStatusCode(HttpStatus.NOT_FOUND);
		    return rv;
		}
		
	}
	
	@GetMapping(value = "/order/checkout/failure")
	public RedirectView publicOrderCheckoutFailed(@RequestParam(value="session_id", required = true) String sessionId) {
		try {
			Session session = Session.retrieve(sessionId);
			String paymentStatus = session.getPaymentStatus();
			String decryptedToken = encDecSymmetric.decryptText(stringToKey(session.getClientReferenceId()));
			String[] params = decryptedToken.split(";");
			String orderUuid = params[0];
			String orderNumber = params[1];
			RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/checkout/failure?orderUuid=" + orderUuid + "&paymentStatus=" + paymentStatus + "&orderNumber=" + orderNumber);
		    rv.setStatusCode(HttpStatus.FOUND);
		    return rv;	
		} catch (StripeException se) {
			RedirectView rv = new RedirectView("com.halalhomemade.app://localhost/public/checkout/failure");
		    rv.setStatusCode(HttpStatus.NOT_FOUND);
		    return rv;
		}
	}
	
}
