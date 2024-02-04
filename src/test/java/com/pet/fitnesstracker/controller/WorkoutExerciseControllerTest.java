package com.pet.fitnesstracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.request.WorkoutExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import com.pet.fitnesstracker.service.WorkoutExerciseService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Elmo Lingad
 */
@WebMvcTest(WorkoutExerciseController.class)
class WorkoutExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutExerciseService service;

    private static final ObjectMapper om = new ObjectMapper();
    private String errorDetail;
    private final WorkoutExerciseRequestDTO requestDTO = new WorkoutExerciseRequestDTO();

    @Test
    void findWorkoutExerciseById_thenSuccess() throws Exception {
        WorkoutExerciseResponseDTO responseDTO = new WorkoutExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setWorkoutName("Test Workout");
        responseDTO.setExerciseName("Test Exercise");
        responseDTO.setRepetitions(1);
        responseDTO.setIntensity("low");
        responseDTO.setRemarks("Test Remarks");

        when(service.findWorkoutExerciseById(anyString())).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/fitness/workouts/exercises/1").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("workoutName").value("Test Workout"))
            .andExpect(jsonPath("exerciseName").value("Test Exercise"))
            .andExpect(jsonPath("repetitions").value(1))
            .andExpect(jsonPath("intensity").value("low"))
            .andExpect(jsonPath("remarks").value("Test Remarks"));
    }

    @Test
    void findWorkoutExerciseById_withAlphabeticValue_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        when(service.findWorkoutExerciseById(anyString())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(get("/v1/fitness/workouts/exercises/invalid").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void findWorkoutExerciseById_whenWorkoutExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Workout Exercise with id '6969' does not exist.";

        when(service.findWorkoutExerciseById(anyString())).thenThrow(new ResourceNotFoundException(errorDetail));

        mockMvc.perform(get("/v1/fitness/workouts/exercises/6969").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("NOT_FOUND"))
            .andExpect(jsonPath("message").value("Requested resource does not exist"));
    }

    @Test
    void addWorkoutExercise_thenSuccess() throws Exception {
        requestDTO.setExerciseId(1L);
        requestDTO.setWorkoutId(1L);
        requestDTO.setRepetitions(1);
        requestDTO.setIntensity("low");
        requestDTO.setRemarks("Test Remarks");

        when(service.addWorkoutExercise(any())).thenReturn(new WorkoutExercise());

        mockMvc.perform(post("/v1/fitness/workouts/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void addWorkoutExercise_whenRequestIsNull_thenFail() throws Exception {
        errorDetail = "Request should not be null";

        when(service.addWorkoutExercise(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/workouts/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(" "))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addWorkoutExercise_whenInvalidRequest_thenFail() throws Exception {
        requestDTO.setRepetitions(-1);

        errorDetail = "Repetitions should be greater than zero";

        when(service.addWorkoutExercise(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/workouts/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void addWorkout_withExistingWorkoutForTrainee_thenFail() throws Exception {
        requestDTO.setWorkoutId(1L);
        requestDTO.setExerciseId(1L);
        requestDTO.setRepetitions(1);
        requestDTO.setIntensity("low");

        errorDetail = "Request violates a specific constraint";

        when(service.addWorkoutExercise(any())).thenThrow(new ConstraintViolationException("cannot execute statement",
            new PSQLException(errorDetail, PSQLState.UNIQUE_VIOLATION), "unq_woex_repetition_intensity"));

        mockMvc.perform(post("/v1/fitness/workouts/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("status").value("CONFLICT"))
            .andExpect(jsonPath("message").value("Request violates a specific constraint - unq_woex_repetition_intensity"));
    }

    @Test
    void deleteWorkoutExercise_thenSuccess() throws Exception {
        doNothing().when(service).deleteWorkoutExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/exercises/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteWorkoutExercise_withAlphabeticInput_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteWorkoutExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/exercises/invalid"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

    @Test
    void deleteWorkoutExercise_whenExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Workout Exercise with id '6969' does not exist.";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteWorkoutExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/exercises/6969"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

}
