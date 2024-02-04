package com.pet.fitnesstracker.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.WorkoutExerciseDTO;
import com.pet.fitnesstracker.dto.request.WorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Elmo Lingad
 */
class WorkoutMapperTest {

    private final WorkoutMapper mapper = Mappers.getMapper(WorkoutMapper.class);
    private final Workout sampleWorkout = createSampleWorkout();
    private final WorkoutResponseDTO expectedResponseDTO = createSampleWorkoutResponseDto();

    WorkoutResponseDTO actualResponseDTO;

    @Test
    void testEntityToDtoMapping() {
        actualResponseDTO = mapper.toDto(sampleWorkout);

        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    void testEntityToDtoMapping_whenWorkoutExercisesIsNull() {
        sampleWorkout.setWorkoutExercises(null);

        actualResponseDTO = mapper.toDto(sampleWorkout);

        assertNull(actualResponseDTO.getExercises());
    }

    @Test
    void testEntityToDtoMapping_whenTraineeIsNull() {
        sampleWorkout.setTrainee(null);

        actualResponseDTO = mapper.toDto(sampleWorkout);

        assertNull(actualResponseDTO.getTraineeName());
    }

    @Test
    void testEntityToDtoMapping_whenTraineeNameIsNull() {
        sampleWorkout.getTrainee().setName(null);

        actualResponseDTO = mapper.toDto(sampleWorkout);

        assertNull(actualResponseDTO.getTraineeName());
    }

    @Test
    void testEntityToDtoMapping_withNullWorkout() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testGettingTraineeName_withNullWorkout() {
        WorkoutMapperImpl mapperImpl = new WorkoutMapperImpl();

        assertNull(ReflectionTestUtils.invokeMethod(mapperImpl, "workoutTraineeName", (Object) null));
    }

    @Test
    void testAddRequestDtoToEntityMapping() {
        WorkoutRequestDTO requestDTO = new WorkoutRequestDTO();

        requestDTO.setName("Test Workout");
        requestDTO.setWorkoutDate("2022-10-14");
        requestDTO.setRemarks("Test Remarks");

        Workout actualWorkout = mapper.toEntity(requestDTO);

        assertEquals("Test Workout", actualWorkout.getName());
        assertEquals("2022-10-14", actualWorkout.getWorkoutDate());
        assertEquals("Test Remarks", actualWorkout.getRemarks());
    }

    @Test
    void testAddRequestDtoToEntityMapping_withNullRequest() {
        assertNull(mapper.toEntity(null));
    }

    private Workout createSampleWorkout() {
        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");

        Workout workout = new Workout();
        WorkoutExercise workoutExercise = createSampleWorkoutExercise();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-14");
        workout.setRemarks("Test Remarks");
        workout.setTrainee(trainee);
        workout.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setWorkout(workout);

        trainee.addWorkout(workout);

        return workout;
    }

    private WorkoutExercise createSampleWorkoutExercise() {
        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setId(1L);
        workoutExercise.setRepetitions(1);
        workoutExercise.setIntensity("low");
        workoutExercise.setRemarks("Test Remarks");

        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Exercise");
        exercise.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }

    private WorkoutResponseDTO createSampleWorkoutResponseDto() {
        WorkoutExerciseDTO workoutExerciseDTO = new WorkoutExerciseDTO();

        workoutExerciseDTO.setId(1L);
        workoutExerciseDTO.setExerciseName("Test Exercise");
        workoutExerciseDTO.setRemarks("Test Remarks");
        workoutExerciseDTO.setIntensity("low");
        workoutExerciseDTO.setRepetitions(1);

        WorkoutResponseDTO responseDTO = new WorkoutResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Workout");
        responseDTO.setWorkoutDate("2022-10-14");
        responseDTO.setTraineeName("Test Trainee");
        responseDTO.setRemarks("Test Remarks");
        responseDTO.setExercises(Collections.singletonList(workoutExerciseDTO));

        return responseDTO;
    }

}
