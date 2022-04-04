package com.halalhomemade.backend.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class AbstractService {

  protected String transformToString(Object value) {
    if (value != null && !value.toString().trim().isEmpty()) {
      if (value instanceof Enum) {
        return ((Enum) value).name().toString();
      }
      return String.valueOf(value);
    }
    return StringUtils.EMPTY;
  }

  protected Date transformToDateTime(Instant instant) {
    if (instant != null) {
      return Date.from(instant);
    }
    return null;
  }

  protected static double transformToDouble(Object value) {
    if (value != null) {
      return new BigDecimal(value.toString()).doubleValue();
    }
    return BigDecimal.ZERO.doubleValue();
  }
}
