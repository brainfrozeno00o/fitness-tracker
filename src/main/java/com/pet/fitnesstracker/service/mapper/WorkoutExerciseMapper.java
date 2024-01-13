package com.pet.fitnesstracker.service.mapper;

import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Elmo Lingad
 */
@Mapper(componentModel = "spring")
public interface WorkoutExerciseMapper {

    @Mapping(target = "workoutName", source = "workout.name")
    @Mapping(target = "exerciseName", source = "exercise.name")
    WorkoutExerciseResponseDTO toDto(WorkoutExercise workoutExercise);

}
