package com.pet.fitnesstracker.controller.exception;

/**
 * @author Elmo Lingad
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}
