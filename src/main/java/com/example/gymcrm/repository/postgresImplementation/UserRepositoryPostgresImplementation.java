package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.User;
import com.example.gymcrm.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryPostgresImplementation implements UserRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<User> findByUsernameAndPassword(String username, String password) {
    Query query = entityManager.createQuery("SELECT u FROM User as u where u.username = :username and u.password = :password");
    query.setParameter("username", username);
    query.setParameter("password", password);
    try {
      User user = (User) query.getSingleResult();
      return Optional.of(user);
    }catch (NoResultException e){
      return Optional.empty();
    }
  }

  @Override
  public void save(User user) {
    if (user.getId() == null) {
      entityManager.persist(user);
    } else {
      entityManager.merge(user);
    }
  }
}
