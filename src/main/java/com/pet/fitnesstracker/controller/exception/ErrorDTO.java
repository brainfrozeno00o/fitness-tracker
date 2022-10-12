package com.pet.fitnesstracker.controller.exception;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author Elmo Lingad
 */
@Data
@AllArgsConstructor
public class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 3499572857036260960L;

    private HttpStatus status;

    private String message;

}
