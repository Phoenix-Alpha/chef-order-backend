package com.halalhomemade.backend.models;

public enum AuthenticationProvider {
  Google("google"),
  Facebook("facebook");

  private String registrationId;

  private AuthenticationProvider(String registrationId) {
    this.registrationId = registrationId;
  }

  public String getRegistrationId() {
    return registrationId;
  }
}
