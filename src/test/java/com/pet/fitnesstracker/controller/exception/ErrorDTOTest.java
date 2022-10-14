package com.pet.fitnesstracker.controller.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.time.Instant;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * @author Elmo Lingad
 */
class ErrorDTOTest {

    @Test
    void checkSetters() {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.PROCESSING, "test message",
            Instant.now().toString());

        String newDateTimeString = Instant.now().toString();

        errorDTO.setStatus(HttpStatus.OK);
        errorDTO.setMessage("test ok message");
        errorDTO.setDateTime(newDateTimeString);

        assertEquals(HttpStatus.OK, errorDTO.getStatus());
        assertEquals("test ok message", errorDTO.getMessage());
        assertEquals(newDateTimeString, errorDTO.getDateTime());
    }

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(ErrorDTO.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(ErrorDTO.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }

}
