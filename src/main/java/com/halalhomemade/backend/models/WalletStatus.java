package com.halalhomemade.backend.models;

public enum WalletStatus {
	INITIAL,				// Initial status. For chefs, creation of connected account not triggered.
	ENABLED,				// Normal status. For chefs, connected account created successfully and verified.
	INVERIFICATION, 		// Wallet is in verification. For chefs, connected account is in verification.
	VERIFICATIONFAILED, 	// Wallet verification failed. For chefs, connected account verification failed.
	SUSPENDED,				// Wallet suspended. For chefs, Stripe payout is disabled and they cannot withdraw money.			
}
