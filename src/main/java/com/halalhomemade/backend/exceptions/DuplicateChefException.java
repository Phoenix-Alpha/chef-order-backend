package com.halalhomemade.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateChefException extends BusinessException {

  public DuplicateChefException(String message) {
    super(message, HttpStatus.CONFLICT, ErrorCode.INVALID_INPUT);
  }
}
