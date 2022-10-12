package com.pet.fitnesstracker.repository;

import com.pet.fitnesstracker.domain.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elmo Lingad
 */
@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

}
