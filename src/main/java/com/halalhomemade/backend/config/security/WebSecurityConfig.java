package com.halalhomemade.backend.config.security;

import static com.halalhomemade.backend.models.AuthenticationProvider.Facebook;
import static com.halalhomemade.backend.models.AuthenticationProvider.Google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
//	@Autowired private JwtRequestFilter jwtRequestFilter;

//	@Autowired private UserDetailsService jwtUserDetailsService;
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
    
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.headers().cacheControl();
        http.csrf()
        	.disable()
        	.cors();
        
        http.csrf()
        	.disable()
        	.authorizeRequests()
        	.antMatchers("**/health").permitAll()
        	.antMatchers("/api/v1/public/**").permitAll()
        	.antMatchers("/api/v1/auth/**").authenticated()
        	//.anyRequest().authenticated()
        	.and()
        	.exceptionHandling()
        	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        	.and()
        	.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
	
//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//
//	    httpSecurity
//	        .csrf()
//	        .disable()
//	        .cors()
//	        .and()
//	        .authorizeRequests()
//	        .antMatchers("/api/v1/auth/logout", "/api/v1/auth/currentuser", "/api/v1/auth/profile/**")
//	        .authenticated()
//	        .regexMatchers("/api/v1/stores/[0-9]+/orders/.*")
//	        .permitAll()
//	        .antMatchers("/api/v1/stores/stripe/verify", "/api/v1/stores/paypal/verify")
//	        .permitAll()
//	        .antMatchers("/api/v1/stripe/webhooks", "/api/v1/stripe/connect/webhooks")
//	        .permitAll()
//	        .antMatchers(
//	            "/api/v1/subscriptions/checkout/stripe/complete",
//	            "/api/v1/subscriptions/checkout/stripe/reject",
//	            "/api/v1/subscriptions/checkout/paypal/complete",
//	            "/api/v1/subscriptions/checkout/paypal/reject")
//	        .permitAll()
//	        .antMatchers("/api/v1/stores/search")
//	        .authenticated()
//	        .regexMatchers("/api/v1/stores/(?=.*[a-zA-Z]).+")
//	        .permitAll()
//	        .regexMatchers("/api/v1/files/.*")
//	        .permitAll()
//	        .antMatchers(
//	            "/api/v1/auth/**",
//	            "/api/v1/refdata/**",
//	            "/api/v1/timezones/**",
//	            "/v2/api-docs",
//	            "/configuration/ui",
//	            "/swagger-resources/**",
//	            "/configuration/security",
//	            "/swagger-ui.html",
//	            "/webjars/**")
//	        .permitAll()
//	        .antMatchers("/api/v1/log")
//	        .permitAll()
//	        .anyRequest()
//	        .authenticated()
//	        .and()
//	        .exceptionHandling()
//	        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//	        .and()
//	        .sessionManagement()
//	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//	
//	    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//	
//	    // Hacky way to add SameSite principle to app cookies till spring-boot properly supports it.
//	    httpSecurity.addFilterAfter(new SameSiteFilter(), BasicAuthenticationFilter.class);
//	}
}
