package com.pet.fitnesstracker.service;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.controller.exception.ResourceNotFoundException;
import com.pet.fitnesstracker.domain.Exercise;
import com.pet.fitnesstracker.domain.Workout;
import com.pet.fitnesstracker.domain.WorkoutExercise;
import com.pet.fitnesstracker.dto.request.AddWorkoutExerciseRequestDTO;
import com.pet.fitnesstracker.dto.response.WorkoutExerciseResponseDTO;
import com.pet.fitnesstracker.repository.ExerciseRepository;
import com.pet.fitnesstracker.repository.WorkoutExerciseRepository;
import com.pet.fitnesstracker.repository.WorkoutRepository;
import com.pet.fitnesstracker.service.mapper.WorkoutExerciseMapper;
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
public class WorkoutExerciseService {

    private final WorkoutExerciseMapper mapper;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutExerciseResponseDTO findWorkoutExerciseById(String id) {
        if (Strings.isBlank(id)) {
            throw new BadRequestException("Id must not be null or empty");
        }

        long workoutExerciseId;

        try {
            workoutExerciseId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Id must be a numeric value");
        }

        Optional<WorkoutExercise> workoutExerciseFromDb = workoutExerciseRepository.findById(workoutExerciseId);

        if (workoutExerciseFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Workout Exercise with id '%s' does not exist.",
                id));
        }

        WorkoutExercise workoutExercise = workoutExerciseFromDb.get();

        log.info("Workout Exercise: {}", workoutExercise);

        return mapper.toDto(workoutExercise);
    }

    public WorkoutExercise addWorkoutExercise(
        AddWorkoutExerciseRequestDTO addWorkoutExerciseRequestDTO) throws ConstraintViolationException {
        if (addWorkoutExerciseRequestDTO == null) {
            throw new BadRequestException("Request should not be null");
        }

        addWorkoutExerciseRequestDTO.validate();

        long workoutId = addWorkoutExerciseRequestDTO.getWorkoutId();

        Optional<Workout> workoutFromDb = workoutRepository.findById(workoutId);

        if (workoutFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Workout with id '%s' does not exist.",
                workoutId));
        }

        long exerciseId = addWorkoutExerciseRequestDTO.getExerciseId();

        Optional<Exercise> exerciseFromDb = exerciseRepository.findById(exerciseId);

        if (exerciseFromDb.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Exercise with id '%s' does not exist.",
                exerciseId));
        }

        WorkoutExercise newWorkoutExercise = new WorkoutExercise();

        newWorkoutExercise.setWorkout(workoutFromDb.get());
        newWorkoutExercise.setExercise(exerciseFromDb.get());
        newWorkoutExercise.setIntensity(addWorkoutExerciseRequestDTO.getIntensity().toLowerCase());
        newWorkoutExercise.setRepetitions(addWorkoutExerciseRequestDTO.getRepetitions());
        newWorkoutExercise.setRemarks(addWorkoutExerciseRequestDTO.getRemarks());

        return workoutExerciseRepository.save(newWorkoutExercise);
    }

}
