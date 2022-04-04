package com.halalhomemade.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SubscriptionCheckoutException extends BusinessException {

  public SubscriptionCheckoutException(String errorMessage) {
    super(errorMessage);
  }
}
