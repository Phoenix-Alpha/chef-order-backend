package com.halalhomemade.backend.models;

public enum OrderStatus {
  DRAFT,				// Order in draft at customer side.
  SUBMITTED,			// Order submitted to the system, not paid.
  RECEIVED,				// Chef received the order.
  APPROVED,				// Chef approved the order. On-demand type requires manual approval, but pre-order type automatically set as approved.
  REJECTED,				// Chef rejected the order.
  CUSTOMERCANCELLED,	// Customer cancelled the order before chef approve/reject it.
  CHEFCANCELLED,		// Chef cancelled the order after approval.
  CONFIRMED,			// Order confirmed by the customer which means it's fulfilled.
  REVIEWED,				// Order got review from the customer.
  DISPUTED,				// Order disputed by the customer.
}
