package com.halalhomemade.backend.exceptions;

public class NoAuthenticationMethodException extends BusinessException {

  public NoAuthenticationMethodException(String errorMessage) {
    super(errorMessage);
  }
}
