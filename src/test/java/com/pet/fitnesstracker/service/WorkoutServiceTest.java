package com.pet.fitnesstracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.pet.fitnesstracker.dto.request.WorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
import com.pet.fitnesstracker.repository.TraineeRepository;
import com.pet.fitnesstracker.repository.WorkoutRepository;
import com.pet.fitnesstracker.service.mapper.WorkoutMapper;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Elmo Lingad
 */
@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    WorkoutMapper mapper;

    @Mock
    WorkoutRepository repository;

    @Mock
    TraineeRepository traineeRepository;

    @InjectMocks
    WorkoutService service;

    BadRequestException badRequestException;
    ResourceNotFoundException resourceNotFoundException;
    WorkoutRequestDTO validRequestDTO = createSampleAddWorkoutRequestDto();

    @Test
    void findWorkoutById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findWorkoutById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findWorkoutById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutById("workout"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void findWorkoutById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.findWorkoutById("6969"));

        assertEquals("Workout with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void findWorkoutById_thenReturnResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(createSampleWorkout()));
        when(mapper.toDto(any())).thenReturn(createSampleWorkoutResponseDto());

        WorkoutResponseDTO actualResponse = service.findWorkoutById("1");

        assertNotNull(actualResponse);

        assertEquals(1L, actualResponse.getId());
        assertEquals("Test Workout", actualResponse.getName());
        assertEquals("2022-10-13", actualResponse.getWorkoutDate());
        assertEquals("Test Trainee", actualResponse.getTraineeName());
        assertEquals("Test Remarks", actualResponse.getRemarks());

        assertTrue(actualResponse.getExercises().isEmpty());
    }

    @Test
    void addWorkout_withNullRequest_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkout(null));

        assertEquals("Request should not be null", badRequestException.getMessage());
    }

    @Test
    void addWorkout_withInvalidValues_thenThrowBadRequest() {
        WorkoutRequestDTO requestDTO = new WorkoutRequestDTO();

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkout(requestDTO));

        assertEquals("Name should not be blank", badRequestException.getMessage());

        requestDTO.setName("Test Workout");

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkout(requestDTO));

        assertEquals("Workout date should not be blank", badRequestException.getMessage());

        requestDTO.setWorkoutDate("now");

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkout(requestDTO));

        assertEquals("Workout date should be in the 'yyyy-MM-dd' format", badRequestException.getMessage());

        requestDTO.setWorkoutDate("2022-10-13");

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkout(requestDTO));

        assertEquals("Trainee ID should not be null", badRequestException.getMessage());
    }

    @Test
    void addWorkout_whenNoTraineeFound_thenThrowResourceNotFound() {
        validRequestDTO.setTraineeId(2L);

        when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.addWorkout(validRequestDTO));

        assertEquals("Trainee with id '2' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void addWorkout_thenReturnNewWorkout() {
        Trainee trainee = createSampleTrainee();
        Workout workout = createSampleWorkout();

        when(traineeRepository.findById(anyLong())).thenReturn(Optional.of(trainee));
        when(mapper.toEntity(any())).thenReturn(workout);

        trainee.addWorkout(workout);

        when(traineeRepository.save(any())).thenReturn(trainee);

        Workout actualWorkout = service.addWorkout(validRequestDTO);

        ArgumentCaptor<Trainee> traineeArgumentCaptor = ArgumentCaptor.forClass(Trainee.class);

        verify(traineeRepository).save(traineeArgumentCaptor.capture());

        Trainee actualTrainee = traineeArgumentCaptor.getValue();

        assertEquals(actualTrainee, actualWorkout.getTrainee());
    }

    @Test
    void deleteWorkoutById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutById("workout-exercise"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.deleteWorkoutById("6969"));

        assertEquals("Workout with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void deleteWorkoutById_thenSuccess() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Workout()));
        doNothing().when(repository).delete(any());

        service.deleteWorkoutById("1");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any());
    }

    @Test
    void updateWorkoutById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.updateWorkout(null, null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void updateWorkoutById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.updateWorkout("", null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void updateWorkoutById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.updateWorkout("trainee", null));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void updateWorkoutById_withValidNumericValue_withNullRequest_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.updateWorkout("1", null));

        assertEquals("Request should not be null", badRequestException.getMessage());
    }

    @Test
    void updateWorkoutById_withValidNumericValue_thenThrowResourceNotFound() {
        validRequestDTO.setName("Updated Test Exercise");

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.updateWorkout("6969", validRequestDTO));

        assertEquals("Workout with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void updateWorkoutById_thenReturnUpdatedExercise() {
        validRequestDTO.setName("Updated Test Workout");
        validRequestDTO.setRemarks("Updated Test Remarks");

        Workout workout = createSampleWorkout();
        Workout updatedWorkout = createSampleWorkout();

        updatedWorkout.setName("Updated Test Workout");
        updatedWorkout.setRemarks("Updated Test Remarks");

        WorkoutResponseDTO expectedResponse = createSampleWorkoutResponseDto();

        expectedResponse.setName("Updated Test Workout");
        expectedResponse.setRemarks("Updated Test Remarks");

        when(repository.findById(anyLong())).thenReturn(Optional.of(workout));
        when(repository.save(any())).thenReturn(updatedWorkout);
        when(mapper.toDto(any())).thenReturn(expectedResponse);

        WorkoutResponseDTO actualResponse = service.updateWorkout("1", validRequestDTO);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).toDto(any());

        assertNotNull(actualResponse);

        assertEquals("Updated Test Workout", actualResponse.getName());
        assertEquals("Updated Test Remarks", actualResponse.getRemarks());
    }

    @Test
    void updateWorkoutById_thenReturnSameExercise() {
        validRequestDTO.setName("Test Workout");
        validRequestDTO.setRemarks("Test Remarks");

        Workout workout = createSampleWorkout();

        when(repository.findById(anyLong())).thenReturn(Optional.of(workout));
        when(mapper.toDto(any())).thenReturn(createSampleWorkoutResponseDto());

        WorkoutResponseDTO actualResponse = service.updateWorkout("1", validRequestDTO);

        verify(repository, times(1)).findById(anyLong());

        assertNotNull(actualResponse);

        assertEquals("Test Workout", actualResponse.getName());
        assertEquals("Test Remarks", actualResponse.getRemarks());
    }

    private Trainee createSampleTrainee() {
        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");

        return trainee;
    }

    private Workout createSampleWorkout() {
        Trainee trainee = createSampleTrainee();
        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-13");
        workout.setRemarks("Test Remarks");
        workout.setTrainee(trainee);

        trainee.addWorkout(workout);

        return workout;
    }

    private WorkoutResponseDTO createSampleWorkoutResponseDto() {
        WorkoutResponseDTO responseDTO = new WorkoutResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Workout");
        responseDTO.setWorkoutDate("2022-10-13");
        responseDTO.setTraineeName("Test Trainee");
        responseDTO.setRemarks("Test Remarks");
        responseDTO.setExercises(new ArrayList<>());

        return responseDTO;
    }

    private WorkoutRequestDTO createSampleAddWorkoutRequestDto() {
        WorkoutRequestDTO requestDTO = new WorkoutRequestDTO();

        requestDTO.setName("Test Workout");
        requestDTO.setWorkoutDate("2022-10-13");
        requestDTO.setTraineeId(1L);
        requestDTO.setRemarks("Test Remarks");

        return requestDTO;
    }

}
