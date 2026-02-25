package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryPostgresImplementation implements UserRepository {
  private final PasswordEncoder passwordEncoder;
  @PersistenceContext
  private EntityManager entityManager;

  public UserRepositoryPostgresImplementation(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Optional<GymUser> findByUsernameAndPassword(String username, String password) {
    Query query = entityManager.createQuery("SELECT u FROM GymUser as u where u.username = :username");
    query.setParameter("username", username);
    try {
      GymUser user = (GymUser) query.getSingleResult();
      if (passwordEncoder.matches(password, user.getPassword())) {
        return Optional.of(user);
      }
      return Optional.empty();
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<GymUser> findByUsername(String username) {
    Query query = entityManager.createQuery("SELECT u FROM GymUser as u where u.username = :username");
    query.setParameter("username", username);
    try {
      GymUser user = (GymUser) query.getSingleResult();
      return Optional.of(user);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void save(GymUser user) {
    if (user.getId() == null) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      entityManager.persist(user);
    } else {
      entityManager.merge(user);
    }
  }

  @Override
  public void updatePassword(GymUser user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    if (user.getId() == null) {
      entityManager.persist(user);
    } else {
      entityManager.merge(user);
    }
  }
}
