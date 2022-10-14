package com.pet.fitnesstracker.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.fitnesstracker.dto.WorkoutExerciseDTO;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class WorkoutResponseDTO implements Serializable {

    private static final long serialVersionUID = -3330916026091631178L;

    private Long id;

    private String name;

    private String traineeName;

    @JsonProperty("date")
    private String workoutDate;

    private String remarks;

    private List<WorkoutExerciseDTO> exercises;

}
