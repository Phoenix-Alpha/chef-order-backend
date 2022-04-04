package com.halalhomemade.backend.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum EuropeanCountryCode {
  AT("AUSTRIA", "AT"),
  BE("BELGIUM", "BE"),
  BG("BULGARIA", "BG"),
  CY("CYPRUS", "CY"),
  CZ("CZECH_REPUBLIC", "CZ"),
  DE("GERMANY", "DE"),
  DK("DENMARK", "DK"),
  EE("ESTONIA", "EE"),
  EL("GREECE", "GR"),
  ES("SPAIN", "ES"),
  FI("FINLAND", "FI"),
  FR("FRANCE", "FR"),
  HR("CROATIA", "HR"),
  HU("HUNGARY", "HU"),
  IE("IRELAND", "IE"),
  IT("ITALY", "IT"),
  LT("LITHUANIA", "LT"),
  LU("LUXEMBOURG", "LU"),
  LV("LATVIA", "LV"),
  MT("MALTA", "MT"),
  NL("THE_NETHERLANDS", "NL"),
  PL("POLAND", "PL"),
  PT("PORTUGAL", "PT"),
  RO("ROMANIA", "RO"),
  SE("SWEDEN", "SE"),
  SI("SLOVENIA", "SI"),
  SK("SLOVAKIA", "SK");

  /** The country */
  private String country;

  private String isoCode;

  /**
   * Instantiating the constant
   *
   * @param country The country name
   * @param isoCode country ISO-2 code
   */
  private EuropeanCountryCode(String country, String isoCode) {
    this.country = country;
    this.isoCode = isoCode;
  }

  /**
   * Gets the country
   *
   * @return The country
   */
  public String getCountry() {
    return country;
  }

  public String getIsoCode() {
    return isoCode;
  }

  /**
   * Checks if any country is present with the specified isoCode
   *
   * @param isoCode The isoCode
   * @return The country isoCode
   */
  public static boolean isCodeValid(String isoCode) {
    return byIsoCode(isoCode).isPresent();
  }

  public static Optional<EuropeanCountryCode> byIsoCode(String isoCode) {
    return Stream.of(EuropeanCountryCode.values())
        .filter(e -> e.isoCode.equalsIgnoreCase(isoCode))
        .findFirst();
  }
}
