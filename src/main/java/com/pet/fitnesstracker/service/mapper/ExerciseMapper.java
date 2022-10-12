package com.pet.fitnesstracker.service.mapper;

import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.WorkoutDTO;
import com.pet.fitnesstracker.dto.WorkoutExerciseDTO;
import com.pet.fitnesstracker.dto.request.AddExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

/**
 * @author Elmo Lingad
 */
@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    @Mapping(target = "workoutExercises", source = "workoutExercises", qualifiedByName = "getWorkouts")
    ExerciseResponseDTO toDto(Exercise exercise);

    Exercise toEntity(AddExerciseRequestDTO addExerciseRequestDTO);

    @Named("getWorkouts")
    static List<WorkoutDTO> getWorkoutExercisesList(List<WorkoutExercise> workoutExercises) {
        if (CollectionUtils.isEmpty(workoutExercises)) {
            return null;
        }

        List<WorkoutDTO> workoutDTOList = new ArrayList<>();

        for (WorkoutExercise workoutExercise : workoutExercises) {
            Workout workout = workoutExercise.getWorkout();
            WorkoutDTO workoutDTO = new WorkoutDTO();

            workoutDTO.setId(workout.getId());
            workoutDTO.setName(workout.getName());
            workoutDTO.setWorkoutDate(workout.getWorkoutDate());
            workoutDTO.setRemarks(workout.getRemarks());

            workoutDTOList.add(workoutDTO);
        }

        return workoutDTOList;
    }

}
