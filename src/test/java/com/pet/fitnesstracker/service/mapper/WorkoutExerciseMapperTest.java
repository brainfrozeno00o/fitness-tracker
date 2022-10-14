package com.pet.fitnesstracker.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Elmo Lingad
 */
class WorkoutExerciseMapperTest {

    private final WorkoutExerciseMapper mapper = Mappers.getMapper(WorkoutExerciseMapper.class);
    private final WorkoutExercise sampleWorkoutExercise = createSampleWorkoutExercise();
    private final WorkoutExerciseResponseDTO expectedResponseDTO = createSampleWorkoutExerciseResponseDto();
    private final WorkoutExerciseMapperImpl mapperImpl = new WorkoutExerciseMapperImpl();

    WorkoutExerciseResponseDTO actualResponseDTO;

    @Test
    void testEntityToDtoMapping() {
        actualResponseDTO = mapper.toDto(sampleWorkoutExercise);

        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    void testEntityToDtoMapping_whenWorkoutIsNull() {
        sampleWorkoutExercise.setWorkout(null);

        actualResponseDTO = mapper.toDto(sampleWorkoutExercise);

        assertNull(actualResponseDTO.getWorkoutName());
    }

    @Test
    void testEntityToDtoMapping_whenWorkoutNameIsNull() {
        sampleWorkoutExercise.getWorkout().setName(null);

        actualResponseDTO = mapper.toDto(sampleWorkoutExercise);

        assertNull(actualResponseDTO.getWorkoutName());
    }

    @Test
    void testEntityToDtoMapping_whenExerciseIsNull() {
        sampleWorkoutExercise.setExercise(null);

        actualResponseDTO = mapper.toDto(sampleWorkoutExercise);

        assertNull(actualResponseDTO.getExerciseName());
    }

    @Test
    void testEntityToDtoMapping_whenExerciseNameIsNull() {
        sampleWorkoutExercise.getExercise().setName(null);

        actualResponseDTO = mapper.toDto(sampleWorkoutExercise);

        assertNull(actualResponseDTO.getExerciseName());
    }

    @Test
    void testEntityToDtoMapping_withNullWorkoutExercise() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testGettingWorkoutName_withNullWorkoutExercise() {
        assertNull(ReflectionTestUtils.invokeMethod(mapperImpl, "workoutExerciseWorkoutName", (Object) null));
    }

    @Test
    void testGettingExerciseName_withNullWorkoutExercise() {
        assertNull(ReflectionTestUtils.invokeMethod(mapperImpl, "workoutExerciseExerciseName", (Object) null));
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
        workout.setWorkoutExercises(Collections.singletonList(workoutExercise));

        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Exercise");
        exercise.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }

    private WorkoutExerciseResponseDTO createSampleWorkoutExerciseResponseDto() {
        WorkoutExerciseResponseDTO responseDTO = new WorkoutExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setWorkoutName("Test Workout");
        responseDTO.setExerciseName("Test Exercise");
        responseDTO.setRepetitions(1);
        responseDTO.setIntensity("low");
        responseDTO.setRemarks("Test Remarks");

        return responseDTO;
    }

}
