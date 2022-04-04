package com.halalhomemade.backend.exceptions;


import com.halalhomemade.backend.validations.ValidationResult;
import java.util.List;
import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -7972111584244822754L;
  private final HttpStatus httpStatus;
  private final ErrorCode errorCode;
  private List<ValidationResult> failedValidations;

  public BusinessException(String errorMessage) {
    this(errorMessage, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT);
  }

  public BusinessException(String errorMessage, ErrorCode errorCode) {
    this(errorMessage, HttpStatus.BAD_REQUEST, errorCode);
  }

  public BusinessException(String errorMessage, List<ValidationResult> failedValidations) {
    this(errorMessage, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_INPUT);
    this.failedValidations = failedValidations;
  }

  public BusinessException(String errorMessage, HttpStatus httpStatus, ErrorCode errorCode) {
    super(errorMessage);
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public List<ValidationResult> getFailedValidations() {
    return failedValidations;
  }
}
