package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.*;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TraineeRepositoryPostgresImplementation implements TraineeRepository {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  @PersistenceContext
  private EntityManager entityManager;

  public TraineeRepositoryPostgresImplementation(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public void save(Trainee trainee) {
    if (trainee.getId() == null) {
      trainee.setPassword(passwordEncoder.encode(trainee.getPassword()));
      entityManager.persist(trainee);
    } else {
      entityManager.merge(trainee);
    }
  }

  @Override
  public void update(Trainee trainee, String username) {
    if (!trainee.getUsername().equals(username)) {
      Optional<GymUser> duplicate = userRepository.findByUsername(trainee.getUsername());
      if (duplicate.isPresent())
        throw new EntityExistsException("user with username:" + trainee.getUsername() + " already exists");
    }
    Trainee oldTrainee = getTraineeByUsername(username).orElseThrow(EntityNotFoundException::new);
    oldTrainee.setUsername(trainee.getUsername());
    oldTrainee.setFirstName(trainee.getFirstName());
    oldTrainee.setLastName(trainee.getLastName());
    if (trainee.getAddress() != null) oldTrainee.setAddress(trainee.getAddress());
    if (trainee.getBirthDate() != null) oldTrainee.setBirthDate(trainee.getBirthDate());
    oldTrainee.setActive(trainee.isActive());
    entityManager.merge(oldTrainee);
  }

  @Override
  public void delete(String username) {
    Trainee trainee = getTraineeByUsername(username).orElseThrow(() -> new RuntimeException("username or password is incorrect"));
    if (entityManager.contains(trainee)) {
      entityManager.remove(trainee);
    } else {
      trainee = entityManager.merge(trainee);
      entityManager.remove(trainee);
    }
  }

  @Override
  public Optional<Trainee> getTraineeByUsername(String username) {
    Query query = entityManager.createQuery("SELECT t FROM GymUser as t WHERE t.username=:username");
    query.setParameter("username", username);
    try {
      Trainee trainee = (Trainee) query.getSingleResult();
      return Optional.of(trainee);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public long countTraineesByUsernameLike(String username) {
    Query query = entityManager.createQuery("SELECT count(t) FROM GymUser as t WHERE t.username like :username");
    query.setParameter("username", username + "%");
    return (Long) query.getSingleResult();
  }

  @Override
  public List<Training> getTrainingsByUsernameAndCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String trainerName, String trainingType) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
    Root<Training> trainingRoot = criteriaQuery.from(Training.class);

    Join<Training, Trainee> traineeJoin = trainingRoot.join("traineeId");
    Join<Training, Trainer> trainerJoin = trainingRoot.join("trainerId");
    Join<Training, TrainingType> trainingTypeJoin = trainingRoot.join("trainingType");
    criteriaQuery.select(trainingRoot)
      .where(builder.equal(traineeJoin.get("username"), username),
        builder.greaterThan(trainingRoot.get("trainingTime"), startDate),
        builder.lessThan(trainingRoot.get("trainingTime"), endDate),
        builder.equal(trainerJoin.get("firstName"), trainerName),
        builder.equal(trainingTypeJoin.get("trainingTypeName"), trainingType)
      );
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public List<Training> getAll() {
    Query query = entityManager.createQuery("SELECT t FROM Training as t");
    return (List<Training>) query.getResultList();
  }

}
