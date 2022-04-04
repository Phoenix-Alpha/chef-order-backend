package com.halalhomemade.backend.services;

import org.springframework.stereotype.Service;

import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.Wallet;
import com.halalhomemade.backend.models.WalletStatus;
import com.halalhomemade.backend.models.WalletType;
import com.halalhomemade.backend.repositories.WalletRepository;
import com.halalhomemade.backend.services.mappers.WalletEntityToWalletResponseMapper;
import com.stripe.model.Account;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletService {
	
	@Autowired WalletRepository walletRepository;
	
	@Autowired WalletEntityToWalletResponseMapper walletToWalletResponseMapper;
	
	public Wallet createChefWallet(Chef chef) {
		Wallet wallet = null;
		Optional<Wallet> existingWallet = walletRepository.findOneByUserId(chef.getUser().getId());
		if (existingWallet.isPresent()) {
			wallet = existingWallet.get();
			wallet.setUser(chef.getUser());
			wallet.setType(WalletType.CHEF);
		} else {
			wallet = Wallet.builder()
				.user(chef.getUser())
				.type(WalletType.CHEF)
				.balance(BigDecimal.ZERO)
				.hold(BigDecimal.ZERO)
				.status(WalletStatus.INITIAL)
				.build();
		}
		Wallet savedWallet = walletRepository.save(wallet);
		return savedWallet;
	}
	
	public Wallet connectStripeAccountToChefWallet(Chef chef, Account account) {
		Wallet wallet = null;
		Optional<Wallet> existingWallet = walletRepository.findOneByUserId(chef.getUser().getId());
		if (existingWallet.isPresent()) {
			wallet = existingWallet.get();
			wallet.setStripeAccountId(account.getId());
		} else {
			// if no wallet, create new and connect stripe account...
			wallet = Wallet.builder()
				.user(chef.getUser())
				.type(WalletType.CHEF)
				.balance(BigDecimal.ZERO)
				.hold(BigDecimal.ZERO)
				.status(WalletStatus.INITIAL)
				.stripeAccountId(account.getId())
				.build();
		}
		Wallet savedWallet = walletRepository.save(wallet);
		return savedWallet;
	}
}