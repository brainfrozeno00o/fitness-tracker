package com.pet.fitnesstracker.controller;

import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.dto.request.AddTraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import com.pet.fitnesstracker.service.TraineeService;
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
    public ResponseEntity<Trainee> addTrainee(@RequestBody AddTraineeRequestDTO addTraineeRequestDTO)
        throws URISyntaxException {
        log.info("Adding trainee: {}", addTraineeRequestDTO);
        Trainee newTrainee = service.addTrainee(addTraineeRequestDTO);

        return ResponseEntity.created(new URI("/v1/fitness/trainees/" + newTrainee.getId())).build();
    }

}
