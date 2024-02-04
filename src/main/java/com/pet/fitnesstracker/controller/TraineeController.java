package com.pet.fitnesstracker.controller;

import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.dto.request.TraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import com.pet.fitnesstracker.service.TraineeService;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping(value = "/v1/fitness/trainees")
@RestController
public class TraineeController {

    private final TraineeService service;

    @GetMapping("/{id}")
    public ResponseEntity<TraineeResponseDTO> getTraineeById(@PathVariable("id") String traineeId) {
        log.info("Retrieving trainee by ID: {}", traineeId);

        return ResponseEntity.ok(service.findTraineeById(traineeId));
    }

    @PostMapping
    public ResponseEntity<Trainee> addTrainee(@RequestBody TraineeRequestDTO traineeRequestDTO)
        throws URISyntaxException {
        log.info("Adding trainee: {}", traineeRequestDTO);
        Trainee newTrainee = service.addTrainee(traineeRequestDTO);

        return ResponseEntity.created(new URI("/v1/fitness/trainees/" + newTrainee.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTraineeById(@PathVariable("id") String traineeId) {
        log.info("Deleting trainee by ID: {}", traineeId);
        service.deleteTraineeById(traineeId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TraineeResponseDTO> updateTrainee(@PathVariable("id") String traineeId,
        @RequestBody TraineeRequestDTO traineeRequestDTO) {
        log.info("Updating trainee with ID: {}", traineeId);

        return ResponseEntity.ok(service.updateTrainee(traineeId, traineeRequestDTO));
    }
}
