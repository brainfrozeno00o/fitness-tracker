package com.pet.fitnesstracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.dto.request.AddTraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import com.pet.fitnesstracker.service.TraineeService;
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
@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService service;

    private static final ObjectMapper om = new ObjectMapper();
    private String errorDetail;
    private final AddTraineeRequestDTO requestDTO = new AddTraineeRequestDTO();

    @Test
    void findTraineeById_thenSuccess() throws Exception {
        TraineeResponseDTO responseDTO = new TraineeResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Trainee");
        responseDTO.setWorkouts(new ArrayList<>());

        when(service.findTraineeById(anyString())).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/fitness/trainees/1").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("name").value("Test Trainee"))
            .andExpect(jsonPath("workouts").isEmpty());
    }

    @Test
    void findTraineeById_withAlphabeticValue_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        when(service.findTraineeById(anyString())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(get("/v1/fitness/trainees/invalid").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void findTraineeById_whenWorkoutExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Trainee with id '6969' does not exist.";

        when(service.findTraineeById(anyString())).thenThrow(new ResourceNotFoundException(errorDetail));

        mockMvc.perform(get("/v1/fitness/trainees/6969").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("NOT_FOUND"))
            .andExpect(jsonPath("message").value("Requested resource does not exist"));
    }

    @Test
    void addTrainee_thenSuccess() throws Exception {
        requestDTO.setName("Test Trainee");

        when(service.addTrainee(any())).thenReturn(new Trainee());

        mockMvc.perform(post("/v1/fitness/trainees")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void addTrainee_whenRequestIsNull_thenFail() throws Exception {
        errorDetail = "Request should not be null";

        when(service.addTrainee(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/trainees")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(" "))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addTrainee_whenInvalidRequest_thenFail() throws Exception {
        requestDTO.setName("");

        errorDetail = "Name should not be blank";

        when(service.addTrainee(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/trainees")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void addTrainee_withExistingTraineeName_thenFail() throws Exception {
        requestDTO.setName("Test Trainee");

        errorDetail = "Request violates a specific constraint";

        when(service.addTrainee(any())).thenThrow(new ConstraintViolationException("cannot execute statement",
            new PSQLException(errorDetail, PSQLState.UNIQUE_VIOLATION), "unq_trainee_name"));

        mockMvc.perform(post("/v1/fitness/trainees")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("status").value("CONFLICT"))
            .andExpect(jsonPath("message").value("Request violates a specific constraint"));
    }

}
