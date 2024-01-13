package com.pet.fitnesstracker.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class WorkoutExerciseDTO implements Serializable {

    private static final long serialVersionUID = -6324094633889471103L;

    private Long id;

    private String exerciseName;

    private int repetitions;

    private String intensity;

    private String remarks;

}
