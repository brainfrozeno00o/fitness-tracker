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
import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.dto.request.AddExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import com.pet.fitnesstracker.service.ExerciseService;
import java.util.ArrayList;
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
@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService service;

    private static final ObjectMapper om = new ObjectMapper();
    private String errorDetail;
    private final AddExerciseRequestDTO requestDTO = new AddExerciseRequestDTO();

    @Test
    void findExerciseById_thenSuccess() throws Exception {
        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Exercise");
        responseDTO.setWorkouts(new ArrayList<>());

        when(service.findExerciseById(anyString())).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/fitness/exercises/1").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("name").value("Test Exercise"))
            .andExpect(jsonPath("workouts").isEmpty());
    }

    @Test
    void findExerciseById_withAlphabeticValue_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        when(service.findExerciseById(anyString())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(get("/v1/fitness/exercises/invalid").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void findExerciseById_whenWorkoutExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Exercise with id '6969' does not exist.";

        when(service.findExerciseById(anyString())).thenThrow(new ResourceNotFoundException(errorDetail));

        mockMvc.perform(get("/v1/fitness/exercises/6969").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("NOT_FOUND"))
            .andExpect(jsonPath("message").value("Requested resource does not exist"));
    }

    @Test
    void addExercise_thenSuccess() throws Exception {
        requestDTO.setName("Test Exercise");

        when(service.addExercise(any())).thenReturn(new Exercise());

        mockMvc.perform(post("/v1/fitness/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void addExercise_whenRequestIsNull_thenFail() throws Exception {
        errorDetail = "Request should not be null";

        when(service.addExercise(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(" "))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addExercise_whenInvalidRequest_thenFail() throws Exception {
        requestDTO.setName("");

        errorDetail = "Name should not be blank";

        when(service.addExercise(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void addExercise_withExistingTraineeName_thenFail() throws Exception {
        requestDTO.setName("Test Exercise");

        errorDetail = "Request violates a specific constraint";

        when(service.addExercise(any())).thenThrow(new ConstraintViolationException("cannot execute statement",
            new PSQLException(errorDetail, PSQLState.UNIQUE_VIOLATION), "unq_exercise_name"));

        mockMvc.perform(post("/v1/fitness/exercises")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("status").value("CONFLICT"))
            .andExpect(jsonPath("message").value("Request violates a specific constraint - unq_exercise_name"));
    }

    @Test
    void deleteExercise_thenSuccess() throws Exception {
        doNothing().when(service).deleteExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/exercises/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteExercise_withAlphabeticInput_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/exercises/invalid"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

    @Test
    void deleteExercise_whenExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Exercise with id '6969' does not exist.";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteExerciseById(anyString());

        mockMvc.perform(delete("/v1/fitness/exercises/6969"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

}
