package com.example.gymcrm.service;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GymUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public GymUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    GymUser user = userRepository.findByUsername(username).orElseThrow();
    if (user instanceof Trainee) {
      return User.withUsername(user.getUsername()).password(user.getPassword()).roles("TRAINEE").build();
    } else if (user instanceof Trainer) {
      return User.withUsername(user.getUsername()).password(user.getPassword()).roles("TRAINER").build();
    }
    throw new UsernameNotFoundException("Unknown user type");
  }
}
