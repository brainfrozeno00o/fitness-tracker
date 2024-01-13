package com.pet.fitnesstracker.dto.response;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class WorkoutExerciseResponseDTO implements Serializable {

    private static final long serialVersionUID = -1079204765962565415L;

    private Long id;

    private String workoutName;

    private String exerciseName;

    private int repetitions;

    private String intensity;

    private String remarks;

}
