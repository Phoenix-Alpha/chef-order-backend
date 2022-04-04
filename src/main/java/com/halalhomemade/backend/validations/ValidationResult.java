package com.halalhomemade.backend.validations;

import com.halalhomemade.backend.exceptions.ErrorCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationResult {
  private boolean isValid;
  private String errorMessage;
  private ErrorCode errorCode;
  private Object additionalInfo;

  public static final ValidationResult ValidResult =
      ValidationResult.builder().isValid(true).build();
}
