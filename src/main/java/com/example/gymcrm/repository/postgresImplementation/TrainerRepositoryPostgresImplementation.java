package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.GymUser;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Transactional
public class TrainerRepositoryPostgresImplementation implements TrainerRepository {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  @PersistenceContext
  private EntityManager entityManager;

  public TrainerRepositoryPostgresImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public void save(Trainer trainer) {
    if (trainer.getId() == null) {
      trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));
      entityManager.persist(trainer);
    } else {
      entityManager.merge(trainer);
    }
  }

  @Override
  public void update(Trainer trainer, String username) {
    if (!trainer.getUsername().equals(username)) {
      Optional<GymUser> duplicate = userRepository.findByUsername(trainer.getUsername());
      if (duplicate.isPresent())
        throw new EntityExistsException("user with username:" + trainer.getUsername() + " already exists");
    }
    Trainer oldTrainer = getTrainerByUsername(username).orElseThrow();
    oldTrainer.setUsername(trainer.getUsername());
    oldTrainer.setFirstName(trainer.getFirstName());
    oldTrainer.setLastName(trainer.getLastName());
    if (trainer.getSpecialization() != null) oldTrainer.setSpecialization(trainer.getSpecialization());
    oldTrainer.setActive(trainer.isActive());
    entityManager.merge(oldTrainer);
  }

  @Override
  public void delete(String username) {
    Trainer trainer = getTrainerByUsername(username).orElseThrow(() -> new RuntimeException("username or password is incorrect"));
    if (entityManager.contains(trainer)) {
      entityManager.remove(trainer);
    } else {
      trainer = entityManager.merge(trainer);
      entityManager.remove(trainer);
    }
  }

  @Override
  public Optional<Trainer> getTrainerByUsername(String username) {
    Query query = entityManager.createQuery("SELECT t FROM GymUser as t WHERE t.username=:username");
    query.setParameter("username", username);
    try {
      Trainer trainer = (Trainer) query.getSingleResult();
      return Optional.of(trainer);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Trainer> getAll() {
    Query query = entityManager.createQuery("SELECT t FROM Trainer as t");
    return (List<Trainer>) query.getResultList();
  }

  @Override
  public long countTrainersByUsernameLike(String username) {
    Query query = entityManager.createQuery("SELECT count(t) FROM GymUser as t WHERE t.username like :username");
    query.setParameter("username", username + "%");
    return (Long) query.getSingleResult();
  }

  @Override
  public List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String traineeName) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
    Root<Training> trainingRoot = criteriaQuery.from(Training.class);

    Join<Training, Trainee> traineeJoin = trainingRoot.join("traineeId");
    Join<Training, Trainer> trainerJoin = trainingRoot.join("trainerId");
    criteriaQuery.select(trainingRoot)
      .where(builder.equal(trainerJoin.get("username"), username),
        builder.greaterThan(trainingRoot.get("trainingTime"), startDate),
        builder.lessThan(trainingRoot.get("trainingTime"), endDate),
        builder.equal(traineeJoin.get("firstName"), traineeName)
      );
    return entityManager.createQuery(criteriaQuery).getResultList();
  }
}
