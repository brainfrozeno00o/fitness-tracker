package com.pet.fitnesstracker.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.dto.WorkoutDTO;
import com.pet.fitnesstracker.dto.request.TraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * @author Elmo Lingad
 */
class TraineeMapperTest {

    private final TraineeMapper mapper = Mappers.getMapper(TraineeMapper.class);
    private final Trainee sampleTrainee = createSampleTrainee();
    private final TraineeResponseDTO expectedResponseDTO = createSampleTraineeResponseDto();

    TraineeResponseDTO actualResponseDTO;

    @Test
    void testEntityToDtoMapping() {
        actualResponseDTO = mapper.toDto(sampleTrainee);

        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    void testEntityToDtoMapping_withNullWorkoutList() {
        sampleTrainee.setWorkouts(null);

        actualResponseDTO = mapper.toDto(sampleTrainee);

        assertNull(actualResponseDTO.getWorkouts());
    }

    @Test
    void testEntityToDtoMapping_withNullElementInWorkoutList() {
        sampleTrainee.setWorkouts(Collections.singletonList(null));

        actualResponseDTO = mapper.toDto(sampleTrainee);

        assertFalse(actualResponseDTO.getWorkouts().isEmpty());
        assertNull(actualResponseDTO.getWorkouts().get(0));
    }

    @Test
    void testEntityToDtoMapping_withNullTrainee() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void testAddRequestDtoToEntityMapping() {
        TraineeRequestDTO requestDTO = new TraineeRequestDTO("Test Trainee");

        Trainee actualTrainee = mapper.toEntity(requestDTO);

        assertEquals("Test Trainee", actualTrainee.getName());
    }

    @Test
    void testAddRequestDtoToEntityMapping_withNullRequest() {
        assertNull(mapper.toEntity(null));
    }

    private Trainee createSampleTrainee() {
        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");

        Workout workout = createSampleWorkout();

        trainee.setWorkouts(Collections.singletonList(workout));

        return trainee;
    }

    private Workout createSampleWorkout() {
        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-14");
        workout.setRemarks("Test Remarks");

        return workout;
    }

    private TraineeResponseDTO createSampleTraineeResponseDto() {
        WorkoutDTO workoutDTO = new WorkoutDTO();

        workoutDTO.setId(1L);
        workoutDTO.setWorkoutDate("2022-10-14");
        workoutDTO.setRemarks("Test Remarks");
        workoutDTO.setName("Test Workout");

        TraineeResponseDTO responseDTO = new TraineeResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Trainee");
        responseDTO.setWorkouts(Collections.singletonList(workoutDTO));

        return responseDTO;
    }

}
