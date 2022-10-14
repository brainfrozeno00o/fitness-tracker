package com.pet.fitnesstracker.repository;

import com.pet.fitnesstracker.domain.Trainee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elmo Lingad
 */
@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {

}
