package com.pet.fitnesstracker.controller;

import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.dto.request.AddExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import com.pet.fitnesstracker.service.ExerciseService;
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
@RequestMapping(value = "/v1/fitness/exercises")
@RestController
public class ExerciseController {

    private final ExerciseService service;

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> getTraineeById(@PathVariable("id") String exerciseId) {
        log.info("Retrieving exercise by ID: {}", exerciseId);

        return ResponseEntity.ok(service.findExerciseById(exerciseId));
    }

    @PostMapping
    public ResponseEntity<Exercise> addExercise(@RequestBody AddExerciseRequestDTO addExerciseRequestDTO)
        throws URISyntaxException {
        log.info("Adding exercise: {}", addExerciseRequestDTO);
        Exercise newExercise = service.addExercise(addExerciseRequestDTO);

        return ResponseEntity.created(new URI("/v1/fitness/exercises/" + newExercise.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExerciseById(@PathVariable("id") String exerciseId) {
        log.info("Deleting exercise by ID: {}", exerciseId);
        service.deleteExerciseById(exerciseId);

        return ResponseEntity.noContent().build();
    }

}
