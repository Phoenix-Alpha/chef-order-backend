package com.halalhomemade.backend.models;

import lombok.Getter;

@Getter
public enum PaymentMethod {
  NO_PAYMENT("", ""),
  CREDIT_CARD_STRIPE("card", "Credit Card"),
  STRIPE_GPAY("stripe_gpay", "Google Pay"),
  STRIPE_APAY("stripe_apay", "Apple Pay"),
  PAYPAL("paypal", "Paypal");
  
  private final String paymentMethod;
  private final String paymentMethodDisplayName;

  PaymentMethod(String paymentMethod, String paymentMethodDisplayName) {
    this.paymentMethod = paymentMethod;
    this.paymentMethodDisplayName = paymentMethodDisplayName;
  }

  public static PaymentMethod from(String paymentMethod) {
    for (PaymentMethod method : PaymentMethod.values()) {
      if (method.paymentMethod.equals(paymentMethod)) {
        return method;
      }
    }
    return PaymentMethod.NO_PAYMENT;
  }
  
}
