package com.pet.fitnesstracker.controller;

import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.dto.request.AddWorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
import com.pet.fitnesstracker.service.WorkoutService;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Elmo Lingad
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/fitness/workouts")
@RestController
public class WorkoutController {

    private final WorkoutService service;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> getWorkoutById(@PathVariable("id") String workoutId) {
        log.info("Retrieving workout by ID: {}", workoutId);

        return ResponseEntity.ok(service.findWorkoutById(workoutId));
    }

    @PostMapping
    public ResponseEntity<Workout> addWorkout(@RequestBody AddWorkoutRequestDTO addWorkoutRequestDTO)
        throws URISyntaxException {
        log.info("Adding workout: {}", addWorkoutRequestDTO);
        Workout newWorkout = service.addWorkout(addWorkoutRequestDTO);

        return ResponseEntity.created(new URI("/v1/fitness/workouts/" + newWorkout.getId())).build();
    }

}
