package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.database.TrainerDatabase;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Trainer;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.model.TrainingType;
import com.example.gymcrm.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
      throw new RuntimeException("trainer already exists");
    }
  }

  @Override
  public void update(Trainer trainer, String username, String password) {
    getTrainerByUsername(username, password);
    entityManager.merge(trainer);
  }

  @Override
  public void delete(String username, String password) {
    Trainer trainer = getTrainerByUsername(username, password);
    if (entityManager.contains(trainer)) {
      entityManager.remove(trainer);
    } else {
      trainer = entityManager.merge(trainer);
      entityManager.remove(trainer);
    }
  }

  @Override
  public Trainer getTrainerByUsername(String username, String password) {
    Query query = entityManager.createQuery("SELECT t FROM Trainer as t WHERE t.username=:username");
    query.setParameter("username", username);
    try {
      return (Trainer) query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long countTrainersByUsernameLike(String username) {
    Query query = entityManager.createQuery("SELECT count(t) FROM Trainer as t WHERE t.username like :username");
    query.setParameter("username", username+"%");
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
        builder.greaterThan(trainingRoot.get("trainingTime"),startDate),
        builder.lessThan(trainingRoot.get("trainingTime"),endDate),
        builder.equal(traineeJoin.get("firstName"),traineeName)
      );
    return entityManager.createQuery(criteriaQuery).getResultList();
  }
}
