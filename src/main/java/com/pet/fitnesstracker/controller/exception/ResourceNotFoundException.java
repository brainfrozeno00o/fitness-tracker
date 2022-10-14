package com.pet.fitnesstracker.controller.exception;

/**
 * @author Elmo Lingad
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
