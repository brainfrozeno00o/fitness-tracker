package com.pet.fitnesstracker.repository;

import com.pet.fitnesstracker.domain.WorkoutExercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elmo Lingad
 */
@Repository
public interface WorkoutExerciseRepository extends CrudRepository<WorkoutExercise, Long> {

}
