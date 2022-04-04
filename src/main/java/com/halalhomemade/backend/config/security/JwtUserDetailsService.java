package com.halalhomemade.backend.config.security;

import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserStatus;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.security.UserPrincipal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findOneByEmail(username);
    if (!user.isPresent()) {
      throw new UsernameNotFoundException(username);
    }
    return new UserPrincipal(user.get());
  }
}
