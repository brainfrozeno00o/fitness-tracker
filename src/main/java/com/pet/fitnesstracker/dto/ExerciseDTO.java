package com.pet.fitnesstracker.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
public class ExerciseDTO implements Serializable {

    private static final long serialVersionUID = 6727276107771197537L;

    private Long id;

    private String name;

}
