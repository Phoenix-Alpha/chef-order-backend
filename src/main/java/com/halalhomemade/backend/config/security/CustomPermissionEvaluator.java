package com.halalhomemade.backend.config.security;

import com.halalhomemade.backend.services.security.UserPrincipal;
import java.io.Serializable;
import java.util.Optional;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
    if ((auth == null)
        || (targetDomainObject == null)
        || !(permission instanceof String)
        || (auth instanceof AnonymousAuthenticationToken)) {
      return false;
    }
    String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

    return hasPrivilege(auth, targetType, Optional.empty(), permission.toString().toUpperCase());
  }

  @Override
  public boolean hasPermission(
      Authentication auth, Serializable targetId, String targetType, Object permission) {
    if ((auth == null)
        || (targetType == null)
        || !(permission instanceof String)
        || (auth instanceof AnonymousAuthenticationToken)) {
      return false;
    }
    return hasPrivilege(
        auth, targetType.toUpperCase(), Optional.of(targetId), permission.toString().toUpperCase());
  }

  private boolean hasPrivilege(
      Authentication auth, String targetType, Optional<Serializable> targetId, String permission) {
    Long userId = ((UserPrincipal) auth.getPrincipal()).getUser().getId();
    String checkPermission =
        targetId
            .map(t -> targetType + "_" + t + "_USER_" + userId + "_ROLE_" + permission)
            .orElse(permission);
    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
      if (grantedAuth.getAuthority().startsWith(targetType)) {
        if (grantedAuth.getAuthority().contains(checkPermission)) {
          return true;
        }
      }
    }
    return false;
  }
}
