package com.pet.fitnesstracker.service;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.dto.request.ExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.ExerciseResponseDTO;
import com.pet.fitnesstracker.repository.ExerciseRepository;
import com.pet.fitnesstracker.service.mapper.ExerciseMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

/**
 * @author Elmo Lingad
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseMapper mapper;
    private final ExerciseRepository repository;

    public ExerciseResponseDTO findExerciseById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long exerciseId;

        try {
            exerciseId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Optional<Exercise> exerciseFromDb = repository.findById(exerciseId);

        if (exerciseFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Exercise with id '%s' does not exist.",
                id));
        }

        Exercise exercise = exerciseFromDb.get();

        log.info("Exercise: {}", exercise);

        return mapper.toDto(exercise);
    }

    public Exercise addExercise(ExerciseRequestDTO exerciseRequestDTO) throws ConstraintViolationException {
        if (exerciseRequestDTO == null) {
            throw new BadRequestException("Request should not be null");
        }

        exerciseRequestDTO.validate();

        return repository.save(mapper.toEntity(exerciseRequestDTO));
    }

    public void deleteExerciseById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long exerciseId;

        try {
            exerciseId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Exercise exerciseFromDb = repository
            .findById(exerciseId)
            .orElse(null);

        if (exerciseFromDb == null) {
            throw new ResourceNotFoundException(String.format("Exercise with id '%s' does not exist.",
                id));
        }

        repository.delete(exerciseFromDb);
    }

}
