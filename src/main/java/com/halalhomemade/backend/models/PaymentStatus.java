package com.halalhomemade.backend.models;

public enum PaymentStatus {
	INITIAL, 					// Initial status.
	IN_PROGRESS,				// Payment for order is in progress.
	SUCCESSFULLY_COMPLETED,		// Payment completed successfully.
	FAILED						// Payment for order failed.
}