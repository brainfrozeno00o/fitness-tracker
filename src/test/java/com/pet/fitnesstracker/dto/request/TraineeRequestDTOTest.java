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
class TraineeRequestDTOTest {

    @Test
    void checkNoArgsConstructorAndGettersAndSetters() {
        assertThat(TraineeRequestDTO.class, allOf(hasValidBeanConstructor(),
            hasValidGettersAndSetters()));
    }

    @Test
    void checkAllArgsConstructor() {
        String testTraineeName = "Test Trainee";
        TraineeRequestDTO traineeRequestDTO = new TraineeRequestDTO(testTraineeName);

        assertEquals(testTraineeName, traineeRequestDTO.getName());
    }

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(TraineeRequestDTO.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(TraineeRequestDTO.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }

}
