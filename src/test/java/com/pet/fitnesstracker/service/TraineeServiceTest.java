package com.pet.fitnesstracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.dto.WorkoutDTO;
import com.pet.fitnesstracker.dto.request.TraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import com.pet.fitnesstracker.repository.TraineeRepository;
import com.pet.fitnesstracker.service.mapper.TraineeMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Elmo Lingad
 */
@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

    @Mock
    TraineeMapper mapper;

    @Mock
    TraineeRepository repository;

    @InjectMocks
    TraineeService service;

    BadRequestException badRequestException;
    ResourceNotFoundException resourceNotFoundException;
    TraineeRequestDTO validRequestDTO = new TraineeRequestDTO("Test Trainee");

    @Test
    void findTraineeById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findTraineeById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findTraineeById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findTraineeById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findTraineeById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findTraineeById("trainee"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void findTraineeById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.findTraineeById("6969"));

        assertEquals("Trainee with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void findTraineeById_thenReturnResponse() {
        Trainee trainee = createSampleTrainee();

        when(repository.findById(anyLong())).thenReturn(Optional.of(trainee));
        when(mapper.toDto(any())).thenReturn(createSampleTraineeResponseDto());

        TraineeResponseDTO actualResponse = service.findTraineeById("1");

        assertNotNull(actualResponse);

        assertEquals(1L, actualResponse.getId());
        assertEquals("Test Trainee", actualResponse.getName());

        assertFalse(actualResponse.getWorkouts().isEmpty());
    }

    @Test
    void addTrainee_withNullRequest_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addTrainee(null));

        assertEquals("Request should not be null", badRequestException.getMessage());
    }

    @Test
    void addTrainee_withInvalidValue_thenThrowBadRequest() {
        TraineeRequestDTO requestDTO = new TraineeRequestDTO();

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addTrainee(requestDTO));

        assertEquals("Name should not be blank", badRequestException.getMessage());
    }

    @Test
    void addTrainee_thenReturnNewTrainee() {
        Trainee trainee = createSampleTrainee();

        trainee.getWorkouts().remove(0);

        when(mapper.toEntity(any())).thenReturn(trainee);
        when(repository.save(any())).thenReturn(trainee);

        Trainee actualTrainee = service.addTrainee(validRequestDTO);

        assertEquals(trainee.getName(), actualTrainee.getName());

        assertTrue(actualTrainee.getWorkouts().isEmpty());
    }

    @Test
    void deleteTraineeById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteTraineeById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteTraineeById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteTraineeById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteTraineeById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteTraineeById("workout-exercise"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void deleteTraineeById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.deleteTraineeById("6969"));

        assertEquals("Trainee with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void deleteTraineeById_thenSuccess() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Trainee()));
        doNothing().when(repository).delete(any());

        service.deleteTraineeById("1");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any());
    }

    private Trainee createSampleTrainee() {
        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");
        trainee.addWorkout(createSampleWorkout());

        return trainee;
    }

    private Workout createSampleWorkout() {
        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-13");
        workout.setRemarks("Test Remarks");

        return workout;
    }

    private TraineeResponseDTO createSampleTraineeResponseDto() {
        WorkoutDTO workoutDTO = new WorkoutDTO();

        workoutDTO.setId(1L);
        workoutDTO.setName("Test Workout");
        workoutDTO.setWorkoutDate("2022-10-13");
        workoutDTO.setRemarks("Test Remarks");

        TraineeResponseDTO responseDTO = new TraineeResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Trainee");
        responseDTO.setWorkouts(Collections.singletonList(workoutDTO));

        return responseDTO;
    }

}
