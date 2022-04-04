package com.halalhomemade.backend.constants;

public interface IApplicationConstants {

  /** The default currency **/
  String DEFAULT_CURRENCY_ISO_CODE = "EUR";
  
  /** Max token generation retries for order **/
  Integer MAX_TOKEN_GENERATION_RETRIES = 20;
  
  /** The order pickup code length */
  Integer ORDER_PICKUP_CODE_LENGTH = 6;

  /** The order number length */
  Integer ORDER_NUMBER_LENGTH = 6;

}
