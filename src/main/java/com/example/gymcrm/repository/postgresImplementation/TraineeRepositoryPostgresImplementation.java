package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TraineeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TraineeRepositoryPostgresImplementation implements TraineeRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void save(Trainee trainee) {
    if (trainee.getId() == null) {
      entityManager.persist(trainee);
    } else {
      entityManager.merge(trainee);
    }
  }

  @Override
  public void update(Trainee trainee, String username, String password) {
    getTraineeByUsername(username, password);
    entityManager.merge(trainee);
  }

  @Override
  public void delete(String username, String password) {
    Trainee trainee = getTraineeByUsername(username, password).orElseThrow(() -> new RuntimeException("username or password is incorrect"));
    if (entityManager.contains(trainee)) {
      entityManager.remove(trainee);
    } else {
      trainee = entityManager.merge(trainee);
      entityManager.remove(trainee);
    }
  }

  @Override
  public Optional<Trainee> getTraineeByUsername(String username, String password) {
    Query query = entityManager.createQuery("SELECT t FROM User as t WHERE t.username=:username and password=:password");
    query.setParameter("username", username);
    query.setParameter("password", password);
    try {
      Trainee trainee = (Trainee) query.getSingleResult();
      return Optional.of(trainee);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public long countTraineesByUsernameLike(String username) {
    Query query = entityManager.createQuery("SELECT count(t) FROM User as t WHERE t.username like :username");
    query.setParameter("username", username + "%");
    return (Long) query.getSingleResult();
  }

  @Override
  public List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String trainerName, String trainingType) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
    Root<Training> trainingRoot = criteriaQuery.from(Training.class);

    Join<Training, Trainee> traineeJoin = trainingRoot.join("traineeId");
    Join<Training, Trainer> trainerJoin = trainingRoot.join("trainerId");
    Join<Training, TrainingType> trainingTypeJoin = trainingRoot.join("trainingType");
    criteriaQuery.select(trainingRoot)
      .where(builder.equal(traineeJoin.get("username"), username),
        builder.equal(traineeJoin.get("password"), password),
        builder.greaterThan(trainingRoot.get("trainingTime"), startDate),
        builder.lessThan(trainingRoot.get("trainingTime"), endDate),
        builder.equal(trainerJoin.get("firstName"), trainerName),
        builder.equal(trainingTypeJoin.get("trainingTypeName"), trainingType)
      );
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public Optional<Trainee> getTraineeByUsername(String traineeUsername) {
    Query query = entityManager.createQuery("SELECT t FROM User as t WHERE t.username=:username");
    query.setParameter("username", traineeUsername);
    try {
      Trainee trainee = (Trainee) query.getSingleResult();
      return Optional.of(trainee);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
