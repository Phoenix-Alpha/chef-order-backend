package com.halalhomemade.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServiceException extends RuntimeException {

  public InternalServiceException(String message) {
    super(message);
  }

  public InternalServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
