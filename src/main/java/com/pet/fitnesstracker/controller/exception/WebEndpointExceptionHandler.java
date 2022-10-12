package com.pet.fitnesstracker.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Elmo Lingad
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@RestControllerAdvice
public class WebEndpointExceptionHandler extends ResponseEntityExceptionHandler {

    private String errorMessage;

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(ConstraintViolationException ex,
        WebRequest request) {
        errorMessage = "Request violates a specific constraint";

        log.error("Constraint Violation On: {}", ex.getConstraintName());
        log.error("Constraint Violation Exception: {}", ex.getMessage());

        return createResponse(new ErrorDTO(HttpStatus.CONFLICT, errorMessage));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
        WebRequest request) {
        errorMessage = "Requested resource does not exist";

        log.error("Resource Not Found Exception: {}", ex.getMessage());

        return createResponse(new ErrorDTO(HttpStatus.NOT_FOUND, errorMessage));
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex,
        WebRequest request) {
        errorMessage = "Request is not well-formed";

        log.error("Bad Request Exception: {}", ex.getMessage());

        return createResponse(new ErrorDTO(HttpStatus.BAD_REQUEST, errorMessage));
    }

    private ResponseEntity<Object> createResponse(ErrorDTO errorDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        return new ResponseEntity<>(errorDTO, headers, errorDTO.getStatus());
    }

}
