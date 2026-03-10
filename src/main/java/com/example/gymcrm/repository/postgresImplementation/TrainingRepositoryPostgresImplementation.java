package com.example.gymcrm.repository.postgresImplementation;

import com.example.gymcrm.database.TrainingDatabase;
import com.example.gymcrm.model.Trainee;
import com.example.gymcrm.model.Training;
import com.example.gymcrm.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@Transactional
public class TrainingRepositoryPostgresImplementation implements TrainingRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void save(Training training) {
    if (training.getId() == null) {
      entityManager.persist(training);
    } else {
      entityManager.merge(training);
    }
  }

  @Override
  public List<Training> getAll() {
    Query query = entityManager.createQuery("SELECT t FROM Training as t");
    List<Training> training = query.getResultList();
    return training;
  }
}
