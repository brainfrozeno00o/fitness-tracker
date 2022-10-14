package com.pet.fitnesstracker.service;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.dto.request.AddWorkoutRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutResponseDTO;
import com.pet.fitnesstracker.repository.TraineeRepository;
import com.pet.fitnesstracker.repository.WorkoutRepository;
import com.pet.fitnesstracker.service.mapper.WorkoutMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 * @author Elmo Lingad
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WorkoutService {

    private final WorkoutMapper mapper;
    private final WorkoutRepository repository;
    // to save a one-to-many entity properly
    private final TraineeRepository traineeRepository;

    public WorkoutResponseDTO findWorkoutById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long workoutId;

        try {
            workoutId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Optional<Workout> workoutFromDb = repository.findById(workoutId);

        if (workoutFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Workout with id '%s' does not exist.",
                id));
        }

        Workout workout = workoutFromDb.get();

        log.info("Workout: {}", workout);

        return mapper.toDto(workout);
    }

    public Workout addWorkout(AddWorkoutRequestDTO addWorkoutRequestDTO) {
        if (addWorkoutRequestDTO == null) {
            throw new BadRequestException("Request should not be null");
        }

        addWorkoutRequestDTO.validate();

        Long traineeId = addWorkoutRequestDTO.getTraineeId();

        Optional<Trainee> traineeFromDb = traineeRepository.findById(traineeId);

        if (traineeFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Trainee with id '%s' does not exist.",
                traineeId));
        }

        Trainee existingTrainee = traineeFromDb.get();
        Workout newWorkout = mapper.toEntity(addWorkoutRequestDTO);

        existingTrainee.addWorkout(newWorkout);
        traineeRepository.save(existingTrainee);

        return newWorkout;
    }

}
