package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.WalletResponse;
import com.halalhomemade.backend.models.Wallet;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class WalletEntityToWalletResponseMapper implements Function<Wallet, WalletResponse> {

	@Override
	public WalletResponse apply(Wallet wallet) {
		WalletResponse response = WalletResponse.builder()
				.id(wallet.getId())
				.userId(wallet.getUser().getId())
				.balance(wallet.getBalance())
				.hold(wallet.getHold())
				.status(wallet.getStatus())
				.type(wallet.getType())
				.stripeAccountId(wallet.getStripeAccountId())
				.build();
		return response;
	}
	
}
