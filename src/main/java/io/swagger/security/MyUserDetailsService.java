package io.swagger.security;

import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{

      User user = userRepository.findByUsername(s);

      if (user == null){
        throw new UsernameNotFoundException("User " + s + " not found.");
      }

      UserDetails userDetails = org.springframework.security.core.userdetails.User
          .withUsername(s)
          .password(user.getPassword())
          .authorities(user.getRoles())
          .accountExpired(false)
          .accountLocked(false)
          .credentialsExpired(false)
          .disabled(false)
          .build();

      return userDetails;
    }
}

