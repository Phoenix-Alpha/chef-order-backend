package com.halalhomemade.backend.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(5)
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/actuator/health");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.antMatcher("/actuator/**")
        .authorizeRequests(
            authorizeRequests -> authorizeRequests.anyRequest().hasRole("SUPER_ADMIN"))
        .httpBasic(Customizer.withDefaults());
  }
}
