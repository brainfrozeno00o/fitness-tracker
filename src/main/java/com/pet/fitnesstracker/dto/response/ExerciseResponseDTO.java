package com.pet.fitnesstracker.dto.response;

import com.pet.fitnesstracker.dto.WorkoutDTO;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class ExerciseResponseDTO implements Serializable {

    private static final long serialVersionUID = 8941919201755182547L;

    private Long id;

    private String name;

    private List<WorkoutDTO> workouts;

}
