package com.pet.fitnesstracker.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class WorkoutDTO implements Serializable {

    private static final long serialVersionUID = -8184904109544656429L;

    private Long id;

    private String name;

    private String workoutDate;

    private String remarks;

}
