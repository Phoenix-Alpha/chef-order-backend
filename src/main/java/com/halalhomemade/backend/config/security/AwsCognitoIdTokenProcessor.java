package com.halalhomemade.backend.config.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;

@Component
@Slf4j
public class AwsCognitoIdTokenProcessor {

    @Autowired
    private JwtConfiguration jwtConfiguration;
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private ConfigurableJWTProcessor configurableJWTProcessor;

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
        if (idToken != null) {
            JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken), null);
            validateIssuer(claims);
            verifyIfIdToken(claims);
            // String username = getUserNameFrom(claims);
            String email = getEmailFrom(claims);
            if (email != null) {
            	try {
            		UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);
            		User user = new User(email, "", userDetails.getAuthorities());
            		return new JwtAuthentication(user, claims, userDetails.getAuthorities());
            	} catch (UsernameNotFoundException e) {
            		System.out.println("UsernameNotFoundException");
            		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
                    // grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
                    User user = new User(email, "", grantedAuthorities);
                    return new JwtAuthentication(user, claims, grantedAuthorities);
            	}
            }
        }
        return null;
    }

    private String getEmailFrom(JWTClaimsSet claims) {
    	return claims.getClaims().get("email").toString();
    }
    
    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}