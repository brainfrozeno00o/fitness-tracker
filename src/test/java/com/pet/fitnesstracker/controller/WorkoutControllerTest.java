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
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.dto.request.WorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
import com.pet.fitnesstracker.service.WorkoutService;
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
@WebMvcTest(WorkoutController.class)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutService service;

    private static final ObjectMapper om = new ObjectMapper();
    private String errorDetail;
    private final WorkoutRequestDTO requestDTO = new WorkoutRequestDTO();

    @Test
    void findWorkoutById_thenSuccess() throws Exception {
        WorkoutResponseDTO responseDTO = new WorkoutResponseDTO();

        responseDTO.setId(1L);
        responseDTO.setName("Test Workout");
        responseDTO.setTraineeName("Test Trainee");
        responseDTO.setRemarks("Test Remarks");
        responseDTO.setWorkoutDate("2022-10-13");
        responseDTO.setExercises(new ArrayList<>());

        when(service.findWorkoutById(anyString())).thenReturn(responseDTO);

        mockMvc.perform(get("/v1/fitness/workouts/1").accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("name").value("Test Workout"))
            .andExpect(jsonPath("traineeName").value("Test Trainee"))
            .andExpect(jsonPath("date").value("2022-10-13"))
            .andExpect(jsonPath("exercises").isEmpty())
            .andExpect(jsonPath("remarks").value("Test Remarks"));
    }

    @Test
    void findWorkoutById_withAlphabeticValue_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        when(service.findWorkoutById(anyString())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(get("/v1/fitness/workouts/invalid").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void findWorkoutById_whenWorkoutExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Workout with id '6969' does not exist.";

        when(service.findWorkoutById(anyString())).thenThrow(new ResourceNotFoundException(errorDetail));

        mockMvc.perform(get("/v1/fitness/workouts/6969").accept(APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("status").value("NOT_FOUND"))
            .andExpect(jsonPath("message").value("Requested resource does not exist"));
    }

    @Test
    void addWorkout_thenSuccess() throws Exception {
        requestDTO.setTraineeId(1L);
        requestDTO.setWorkoutDate("2022-10-13");
        requestDTO.setName("Test Workout");
        requestDTO.setRemarks("Test Remarks");

        when(service.addWorkout(any())).thenReturn(new Workout());

        mockMvc.perform(post("/v1/fitness/workouts")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void addWorkout_whenRequestIsNull_thenFail() throws Exception {
        errorDetail = "Request should not be null";

        when(service.addWorkout(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/workouts")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(" "))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void addWorkout_whenInvalidRequest_thenFail() throws Exception {
        requestDTO.setWorkoutDate("now");

        errorDetail = "Workout date should be in the 'yyyy-MM-dd' format";

        when(service.addWorkout(any())).thenThrow(new BadRequestException(errorDetail));

        mockMvc.perform(post("/v1/fitness/workouts")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"))
            .andExpect(jsonPath("message").value("Request is not well-formed"));
    }

    @Test
    void addWorkout_withExistingWorkoutForTrainee_thenFail() throws Exception {
        requestDTO.setTraineeId(1L);
        requestDTO.setName("Test Workout");
        requestDTO.setWorkoutDate("2022-10-13");

        errorDetail = "Request violates a specific constraint";

        when(service.addWorkout(any())).thenThrow(new ConstraintViolationException("cannot execute statement",
            new PSQLException(errorDetail, PSQLState.UNIQUE_VIOLATION), "unq_trainee_workout_name_day"));

        mockMvc.perform(post("/v1/fitness/workouts")
                .contentType(APPLICATION_JSON)
                .content(om.writeValueAsString(requestDTO))
                .accept(APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("status").value("CONFLICT"))
            .andExpect(jsonPath("message").value("Request violates a specific constraint - unq_trainee_workout_name_day"));
    }

    @Test
    void deleteWorkout_thenSuccess() throws Exception {
        doNothing().when(service).deleteWorkoutById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteWorkout_withAlphabeticInput_thenFail() throws Exception {
        errorDetail = "Id must be a numeric value";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteWorkoutById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/invalid"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

    @Test
    void deleteWorkout_whenExerciseIdIsNonExistent_thenFail() throws Exception {
        errorDetail = "Workout with id '6969' does not exist.";

        doThrow(new BadRequestException(errorDetail)).when(service).deleteWorkoutById(anyString());

        mockMvc.perform(delete("/v1/fitness/workouts/6969"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

}
