package com.pet.fitnesstracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.request.AddWorkoutExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import com.pet.fitnesstracker.repository.ExerciseRepository;
import com.pet.fitnesstracker.repository.WorkoutExerciseRepository;
import com.pet.fitnesstracker.repository.WorkoutRepository;
import com.pet.fitnesstracker.service.mapper.WorkoutExerciseMapper;
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
class WorkoutExerciseServiceTest {

    @Mock
    WorkoutExerciseMapper mapper;

    @Mock
    ExerciseRepository exerciseRepository;

    @Mock
    WorkoutRepository workoutRepository;

    @Mock
    WorkoutExerciseRepository repository;

    @InjectMocks
    WorkoutExerciseService service;

    BadRequestException badRequestException;
    ResourceNotFoundException resourceNotFoundException;
    AddWorkoutExerciseRequestDTO validRequestDTO = createSampleAddWorkoutExerciseRequestDto();

    @Test
    void findWorkoutExerciseById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutExerciseById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findWorkoutExerciseById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutExerciseById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findWorkoutExerciseById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findWorkoutExerciseById("workout-exercise"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void findWorkoutExerciseById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.findWorkoutExerciseById("6969"));

        assertEquals("Workout Exercise with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void findWorkoutExerciseById_thenReturnResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(createSampleWorkoutExercise()));
        when(mapper.toDto(any())).thenReturn(createSampleWorkoutExerciseResponseDto());

        WorkoutExerciseResponseDTO actualResponse = service.findWorkoutExerciseById("1");

        assertNotNull(actualResponse);

        assertEquals(1L, actualResponse.getId());
        assertEquals("Test Workout", actualResponse.getWorkoutName());
        assertEquals("Test Exercise", actualResponse.getExerciseName());
        assertEquals(1, actualResponse.getRepetitions());
        assertEquals("low", actualResponse.getIntensity());
        assertEquals("Test Remarks", actualResponse.getRemarks());
    }

    @Test
    void addWorkoutExercise_withNullRequest_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(null));

        assertEquals("Request should not be null", badRequestException.getMessage());
    }

    @Test
    void addWorkoutExercise_withInvalidValues_thenThrowBadRequest() {
        AddWorkoutExerciseRequestDTO requestDTO = new AddWorkoutExerciseRequestDTO();

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(requestDTO));

        assertEquals("Workout ID should not be null", badRequestException.getMessage());

        requestDTO.setWorkoutId(1L);

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(requestDTO));

        assertEquals("Exercise ID should not be null", badRequestException.getMessage());

        requestDTO.setExerciseId(1L);

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(requestDTO));

        assertEquals("Repetitions should be greater than zero", badRequestException.getMessage());

        requestDTO.setRepetitions(2);

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(requestDTO));

        assertEquals("Intensity should not be blank", badRequestException.getMessage());

        requestDTO.setIntensity("intense");

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addWorkoutExercise(requestDTO));

        assertEquals("Intensity should either be Low, Medium or High", badRequestException.getMessage());
    }

    @Test
    void addWorkoutExercise_whenNoWorkoutFound_thenThrowResourceNotFound() {
        validRequestDTO.setWorkoutId(2L);

        when(workoutRepository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.addWorkoutExercise(validRequestDTO));

        assertEquals("Workout with id '2' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void addWorkoutExercise_whenNoExerciseFound_thenThrowResourceNotFound() {
        validRequestDTO.setExerciseId(2L);

        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(createSampleWorkout()));
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.addWorkoutExercise(validRequestDTO));

        assertEquals("Exercise with id '2' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void addWorkoutExercise_thenReturnNewWorkoutExercise() {
        when(workoutRepository.findById(anyLong())).thenReturn(Optional.of(createSampleWorkout()));
        when(exerciseRepository.findById(anyLong())).thenReturn(Optional.of(createSampleExercise()));
        when(repository.save(any())).thenReturn(createSampleWorkoutExercise());

        WorkoutExercise actualWorkoutExercise = service.addWorkoutExercise(validRequestDTO);

        assertNotNull(actualWorkoutExercise);
        assertNotNull(actualWorkoutExercise.getExercise());
        assertNotNull(actualWorkoutExercise.getWorkout());

        assertEquals(1L, actualWorkoutExercise.getId());
        assertEquals("Test Workout", actualWorkoutExercise.getWorkout().getName());
        assertEquals("Test Exercise", actualWorkoutExercise.getExercise().getName());
        assertEquals(1, actualWorkoutExercise.getRepetitions());
        assertEquals("low", actualWorkoutExercise.getIntensity());
        assertEquals("Test Remarks", actualWorkoutExercise.getRemarks());
    }

    @Test
    void deleteWorkoutExerciseById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutExerciseById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutExerciseById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutExerciseById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutExerciseById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteWorkoutExerciseById("workout-exercise"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void deleteWorkoutExerciseById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.deleteWorkoutExerciseById("6969"));

        assertEquals("Workout Exercise with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void deleteWorkoutExerciseById_thenSuccess() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new WorkoutExercise()));
        doNothing().when(repository).delete(any());

        service.deleteWorkoutExerciseById("1");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any());
    }

    private Workout createSampleWorkout() {
        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");

        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-13");
        workout.setRemarks("Test Remarks");
        workout.setTrainee(trainee);

        trainee.addWorkout(workout);

        return workout;
    }

    private Exercise createSampleExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Exercise");

        return exercise;
    }

    private WorkoutExercise createSampleWorkoutExercise() {
        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setId(1L);
        workoutExercise.setRepetitions(1);
        workoutExercise.setIntensity("low");
        workoutExercise.setRemarks("Test Remarks");

        Exercise exercise = createSampleExercise();
        Workout workout = createSampleWorkout();

        exercise.setWorkoutExercises(Collections.singletonList(workoutExercise));
        workout.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setExercise(exercise);
        workoutExercise.setWorkout(workout);

        return workoutExercise;
    }

    private WorkoutExerciseResponseDTO createSampleWorkoutExerciseResponseDto() {
        WorkoutExerciseResponseDTO responseDTO = new WorkoutExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setExerciseName("Test Exercise");
        responseDTO.setWorkoutName("Test Workout");
        responseDTO.setRepetitions(1);
        responseDTO.setIntensity("low");
        responseDTO.setRemarks("Test Remarks");

        return responseDTO;
    }

    private AddWorkoutExerciseRequestDTO createSampleAddWorkoutExerciseRequestDto() {
        AddWorkoutExerciseRequestDTO requestDTO = new AddWorkoutExerciseRequestDTO();

        requestDTO.setExerciseId(1L);
        requestDTO.setWorkoutId(1L);
        requestDTO.setRepetitions(1);
        requestDTO.setIntensity("low");
        requestDTO.setRemarks("Test Remarks");

        return requestDTO;
    }

}
