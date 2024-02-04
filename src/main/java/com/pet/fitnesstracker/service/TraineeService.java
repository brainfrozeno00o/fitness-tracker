package com.pet.fitnesstracker.service;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.dto.request.TraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import com.pet.fitnesstracker.repository.TraineeRepository;
import com.pet.fitnesstracker.service.mapper.TraineeMapper;
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
public class TraineeService {

    private final TraineeMapper mapper;
    private final TraineeRepository repository;

    public TraineeResponseDTO findTraineeById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long traineeId;

        try {
            traineeId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Optional<Trainee> traineeFromDb = repository.findById(traineeId);

        if (traineeFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Trainee with id '%s' does not exist.",
                id));
        }

        Trainee trainee = traineeFromDb.get();

        log.info("Trainee: {}", trainee);

        return mapper.toDto(trainee);
    }

    public Trainee addTrainee(TraineeRequestDTO traineeRequestDTO) throws ConstraintViolationException {
        if (traineeRequestDTO == null) {
            throw new BadRequestException("Request should not be null");
        }

        traineeRequestDTO.validate();

        return repository.save(mapper.toEntity(traineeRequestDTO));
    }

    public void deleteTraineeById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long traineeId;

        try {
            traineeId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Trainee traineeFromDb = repository
            .findById(traineeId)
            .orElse(null);

        if (traineeFromDb == null) {
            throw new ResourceNotFoundException(String.format("Trainee with id '%s' does not exist.",
                id));
        }

        repository.delete(traineeFromDb);
    }

}
