package com.pet.fitnesstracker.repository;

import com.pet.fitnesstracker.domain.Workout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elmo Lingad
 */
@Repository
public interface WorkoutRepository extends CrudRepository<Workout, Long> {

}
