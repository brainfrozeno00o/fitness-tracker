package com.pet.fitnesstracker.controller;

import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.request.WorkoutExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import com.pet.fitnesstracker.service.WorkoutExerciseService;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = "/v1/fitness/workouts/exercises")
@RestController
public class WorkoutExerciseController {

    private final WorkoutExerciseService service;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutExerciseResponseDTO> getWorkoutExerciseById(@PathVariable("id") String workoutExerciseId) {
        log.info("Retrieving workout exercise by ID: {}", workoutExerciseId);

        return ResponseEntity.ok(service.findWorkoutExerciseById(workoutExerciseId));
    }

    @PostMapping
    public ResponseEntity<WorkoutExercise> addWorkoutExercise(@RequestBody WorkoutExerciseRequestDTO workoutExerciseRequestDTO)
        throws URISyntaxException {
        log.info("Adding workout exercise: {}", workoutExerciseRequestDTO);
        WorkoutExercise newWorkoutExercise = service.addWorkoutExercise(workoutExerciseRequestDTO);

        return ResponseEntity.created(new URI("/v1/fitness/workouts/exercises/" + newWorkoutExercise.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutExerciseById(@PathVariable("id") String workoutExerciseId) {
        log.info("Deleting workout exercise by ID: {}", workoutExerciseId);
        service.deleteWorkoutExerciseById(workoutExerciseId);

        return ResponseEntity.noContent().build();
    }

}
