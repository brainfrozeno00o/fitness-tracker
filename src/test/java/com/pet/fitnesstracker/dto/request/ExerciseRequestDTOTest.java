package com.pet.fitnesstracker.dto.request;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class ExerciseRequestDTOTest {

    @Test
    void checkNoArgsConstructorAndGettersAndSetters() {
        assertThat(ExerciseRequestDTO.class, allOf(hasValidBeanConstructor(),
            hasValidGettersAndSetters()));
    }

    @Test
    void checkAllArgsConstructor() {
        String testExerciseName = "Test Exercise";
        ExerciseRequestDTO exerciseRequestDTO = new ExerciseRequestDTO(testExerciseName);

        assertEquals(testExerciseName, exerciseRequestDTO.getName());
    }

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(ExerciseRequestDTO.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(ExerciseRequestDTO.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }

}
