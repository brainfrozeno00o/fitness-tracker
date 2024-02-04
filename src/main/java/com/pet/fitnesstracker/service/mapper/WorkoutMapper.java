package com.pet.fitnesstracker.service.mapper;

import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.WorkoutExerciseDTO;
import com.pet.fitnesstracker.dto.request.WorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
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
public interface WorkoutMapper {

    @Mapping(target = "traineeName", source = "trainee.name")
    @Mapping(target = "exercises", source = "workoutExercises", qualifiedByName = "getWorkoutExercises")
    WorkoutResponseDTO toDto(Workout workout);

    Workout toEntity(WorkoutRequestDTO workoutRequestDTO);

    @Named("getWorkoutExercises")
    static List<WorkoutExerciseDTO> getWorkoutExercisesList(List<WorkoutExercise> workoutExercises) {
        if (CollectionUtils.isEmpty(workoutExercises)) {
            return null;
        }

        List<WorkoutExerciseDTO> workoutExerciseDTOList = new ArrayList<>();

        for (WorkoutExercise workoutExercise : workoutExercises) {
            WorkoutExerciseDTO workoutExerciseDTO = new WorkoutExerciseDTO();

            workoutExerciseDTO.setId(workoutExercise.getId());
            workoutExerciseDTO.setExerciseName(workoutExercise.getExercise().getName());
            workoutExerciseDTO.setRepetitions(workoutExercise.getRepetitions());
            workoutExerciseDTO.setIntensity(workoutExercise.getIntensity());
            workoutExerciseDTO.setRemarks(workoutExercise.getRemarks());

            workoutExerciseDTOList.add(workoutExerciseDTO);
        }

        return workoutExerciseDTOList;
    }

}
