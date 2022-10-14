package com.pet.fitnesstracker.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.WorkoutDTO;
import com.pet.fitnesstracker.dto.request.AddExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * @author Elmo Lingad
 */
class ExerciseMapperTest {

    private final ExerciseMapper mapper = Mappers.getMapper(ExerciseMapper.class);
    private final Exercise sampleExercise = createSampleExercise();
    private final ExerciseResponseDTO expectedResponseDTO = createSampleExerciseResponseDto();

    ExerciseResponseDTO actualResponseDTO;

    @Test
    void testEntityToDtoMapping() {
        actualResponseDTO = mapper.toDto(sampleExercise);

        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    void testEntityToDtoMapping_whenWorkoutExercisesIsNull() {
        sampleExercise.setWorkoutExercises(null);

        actualResponseDTO = mapper.toDto(sampleExercise);

        assertNull(actualResponseDTO.getWorkoutExercises());
    }

    @Test
    void testEntityToDtoMapping_withNullExercise() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testAddRequestDtoToEntityMapping() {
        AddExerciseRequestDTO requestDTO = new AddExerciseRequestDTO("Test Exercise");

        Exercise actualExercise = mapper.toEntity(requestDTO);

        assertEquals("Test Exercise", actualExercise.getName());
    }

    @Test
    void testAddRequestDtoToEntityMapping_withNullRequest() {
        assertNull(mapper.toEntity(null));
    }

    private Exercise createSampleExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Exercise");

        WorkoutExercise workoutExercise = createSampleWorkoutExercise();

        workoutExercise.setExercise(exercise);

        exercise.setWorkoutExercises(Collections.singletonList(workoutExercise));

        return exercise;
    }

    private WorkoutExercise createSampleWorkoutExercise() {
        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setId(1L);
        workoutExercise.setRepetitions(1);
        workoutExercise.setIntensity("low");
        workoutExercise.setRemarks("Test Remarks");

        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-14");
        workout.setRemarks("Test Remarks");
        workout.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setWorkout(workout);

        return workoutExercise;
    }

    private ExerciseResponseDTO createSampleExerciseResponseDto() {
        WorkoutDTO workoutDTO = new WorkoutDTO();

        workoutDTO.setId(1L);
        workoutDTO.setWorkoutDate("2022-10-14");
        workoutDTO.setRemarks("Test Remarks");
        workoutDTO.setName("Test Workout");

        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Exercise");
        responseDTO.setWorkoutExercises(Collections.singletonList(workoutDTO));

        return responseDTO;
    }

}
