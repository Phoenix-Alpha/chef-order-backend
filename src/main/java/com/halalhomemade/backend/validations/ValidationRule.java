package com.halalhomemade.backend.validations;

public interface ValidationRule {
  ValidationResult validate(Object object);
}
