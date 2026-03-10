package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainerRepository;
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
@Primary
@Transactional
public class TrainerRepositoryPostgresImplementation implements TrainerRepository {
  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public void save(Trainer trainer) {
    if (trainer.getId() == null) {
      entityManager.persist(trainer);
    } else {
      entityManager.merge(trainer);
    }
  }

  @Override
  public void update(Trainer trainer, String username, String password) {
    getTrainerByUsername(username, password);
    entityManager.merge(trainer);
  }

  @Override
  public void delete(String username, String password) {
    Trainer trainer = getTrainerByUsername(username, password).orElseThrow(() -> new RuntimeException("username or password is incorrect"));
    if (entityManager.contains(trainer)) {
      entityManager.remove(trainer);
    } else {
      trainer = entityManager.merge(trainer);
      entityManager.remove(trainer);
    }
  }

  @Override
  public Optional<Trainer> getTrainerByUsername(String username, String password) {
    Query query = entityManager.createQuery("SELECT t FROM User as t WHERE t.username=:username and password=:password");
    query.setParameter("username", username);
    query.setParameter("password", password);
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
    Query query = entityManager.createQuery("SELECT count(t) FROM User as t WHERE t.username like :username");
    query.setParameter("username", username + "%");
    return (Long) query.getSingleResult();
  }

  @Override
  public List<Training> getTrainingsByUsernameAndCriteria(String username, String password, LocalDateTime startDate, LocalDateTime endDate, String traineeName) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
    Root<Training> trainingRoot = criteriaQuery.from(Training.class);

    Join<Training, Trainee> traineeJoin = trainingRoot.join("traineeId");
    Join<Training, Trainer> trainerJoin = trainingRoot.join("trainerId");
    criteriaQuery.select(trainingRoot)
      .where(builder.equal(trainerJoin.get("username"), username),
        builder.equal(trainerJoin.get("password"), password),
        builder.greaterThan(trainingRoot.get("trainingTime"), startDate),
        builder.lessThan(trainingRoot.get("trainingTime"), endDate),
        builder.equal(traineeJoin.get("firstName"), traineeName)
      );
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

  @Override
  public Optional<Trainer> getTrainerByUsername(String username) {
    Query query = entityManager.createQuery("SELECT t FROM User as t WHERE t.username=:username");
    query.setParameter("username", username);
    try {
      Trainer trainer = (Trainer) query.getSingleResult();
      return Optional.of(trainer);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
