package com.halalhomemade.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends BusinessException {
  public LanguageNotFoundException(String errorMessage) {
    super(errorMessage, HttpStatus.NOT_FOUND, ErrorCode.LANGUAGE_NOT_FOUND);
  }
}
