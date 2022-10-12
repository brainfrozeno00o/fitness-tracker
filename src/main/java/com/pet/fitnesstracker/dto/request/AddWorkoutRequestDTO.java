package com.pet.fitnesstracker.dto.request;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import lombok.Data;
import org.apache.commons.validator.GenericValidator;
import org.apache.logging.log4j.util.Strings;

/**
 * @author Elmo Lingad
 */
@Data
public class AddWorkoutRequestDTO implements RequestDTO {

    private static final long serialVersionUID = -8407866699465847081L;

    private String name;

    private String workoutDate;

    private String remarks;

    private Long traineeId;

    @Override
    public void validate() {
        if (Strings.isBlank(name)) {
            throw new BadRequestException("Name should not be blank");
        }

        if (Strings.isBlank(workoutDate)) {
            throw new BadRequestException("Workout date should not be blank");
        }

        if (!GenericValidator.isDate(workoutDate, "yyyy-MM-dd", true)) {
            throw new BadRequestException("Workout date should be in the 'yyyy-MM-dd' format");
        }

        if (traineeId == null) {
            throw new BadRequestException("Trainee ID should not be null");
        }
    }
}
