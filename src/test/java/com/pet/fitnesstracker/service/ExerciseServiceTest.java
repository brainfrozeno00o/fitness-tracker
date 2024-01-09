package com.pet.fitnesstracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.pet.fitnesstracker.dto.request.AddExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import com.pet.fitnesstracker.repository.ExerciseRepository;
import com.pet.fitnesstracker.service.mapper.ExerciseMapper;
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
public class ExerciseServiceTest {

    @Mock
    ExerciseMapper mapper;

    @Mock
    ExerciseRepository repository;

    @InjectMocks
    ExerciseService service;

    BadRequestException badRequestException;
    ResourceNotFoundException resourceNotFoundException;
    AddExerciseRequestDTO validRequestDTO = new AddExerciseRequestDTO("Test Exercise");

    @Test
    void findExerciseById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findExerciseById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findExerciseById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findExerciseById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void findExerciseById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.findExerciseById("trainee"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void findExerciseById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        final ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.findExerciseById("6969"));

        assertEquals("Exercise with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void findExerciseById_thenReturnResponse() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(createSampleExercise()));
        when(mapper.toDto(any())).thenReturn(createSampleExerciseResponseDto());

        ExerciseResponseDTO actualResponse = service.findExerciseById("1");

        assertNotNull(actualResponse);

        assertEquals(1L, actualResponse.getId());
        assertEquals("Test Exercise", actualResponse.getName());

        assertNull(actualResponse.getWorkouts());
    }

    @Test
    void addExercise_withNullRequest_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addExercise(null));

        assertEquals("Request should not be null", badRequestException.getMessage());
    }

    @Test
    void addExercise_withInvalidValue_thenThrowBadRequest() {
        AddExerciseRequestDTO requestDTO = new AddExerciseRequestDTO();

        badRequestException = assertThrows(BadRequestException.class, () ->
            service.addExercise(requestDTO));

        assertEquals("Name should not be blank", badRequestException.getMessage());
    }

    @Test
    void addExercise_thenReturnNewTrainee() {
        Exercise exercise = createSampleExercise();

        when(mapper.toEntity(any())).thenReturn(exercise);
        when(repository.save(any())).thenReturn(exercise);

        Exercise actualExercise = service.addExercise(validRequestDTO);

        assertEquals(exercise.getName(), actualExercise.getName());

        assertNull(actualExercise.getWorkoutExercises());
    }

    @Test
    void deleteExerciseById_withNullString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteExerciseById(null));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteExerciseById_withEmptyString_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteExerciseById(""));

        assertEquals("Id must not be null or empty", badRequestException.getMessage());
    }

    @Test
    void deleteExerciseById_withInvalidNumericValue_thenThrowBadRequest() {
        badRequestException = assertThrows(BadRequestException.class, () ->
            service.deleteExerciseById("workout-exercise"));

        assertEquals("Id must be a numeric value", badRequestException.getMessage());
    }

    @Test
    void deleteExerciseById_withValidNumericValue_thenThrowResourceNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
            service.deleteExerciseById("6969"));

        assertEquals("Exercise with id '6969' does not exist.", resourceNotFoundException.getMessage());
    }

    @Test
    void deleteExerciseById_thenSuccess() throws Exception {
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Exercise()));
        doNothing().when(repository).delete(any());

        service.deleteExerciseById("1");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any());
    }

    private Exercise createSampleExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Exercise");

        return exercise;
    }

    private ExerciseResponseDTO createSampleExerciseResponseDto() {
        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Exercise");

        return responseDTO;
    }

}
