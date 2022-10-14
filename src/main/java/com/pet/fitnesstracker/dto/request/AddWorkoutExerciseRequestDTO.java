package com.pet.fitnesstracker.dto.request;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import com.pet.fitnesstracker.domain.enumeration.Intensity;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * @author Elmo Lingad
 */
@Data
public class AddWorkoutExerciseRequestDTO implements RequestDTO {

    private static final long serialVersionUID = 6383076832436818842L;

    private Long workoutId;

    private Long exerciseId;

    private int repetitions;

    private String intensity;

    private String remarks;

    @Override
    public void validate() {
        if (workoutId == null) {
            throw new BadRequestException("Workout ID should not be null");
        }

        if (exerciseId == null) {
            throw new BadRequestException("Exercise ID should not be null");
        }
        // limit is subject to change
        if (repetitions < 1) {
            throw new BadRequestException("Repetitions should be greater than zero");
        }

        if (Strings.isBlank(intensity)) {
            throw new BadRequestException("Intensity should not be blank");
        }
        // predefined intensities are subject to change
        if (!Intensity.isValidIntensity(intensity)) {
            throw new BadRequestException("Intensity should either be Low, Medium or High");
        }
    }
}
