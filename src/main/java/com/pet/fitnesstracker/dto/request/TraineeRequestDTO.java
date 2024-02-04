package com.pet.fitnesstracker.dto.request;

import com.pet.fitnesstracker.controller.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

/**
 * @author Elmo Lingad
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRequestDTO implements RequestDTO {

    private static final long serialVersionUID = -1808415223405440930L;

    private String name;

    @Override
    public void validate() {
        if (Strings.isBlank(name)) {
            throw new BadRequestException("Name should not be blank");
        }
    }

}
