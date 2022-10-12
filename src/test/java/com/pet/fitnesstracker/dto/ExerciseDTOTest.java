package com.pet.fitnesstracker.dto;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class ExerciseDTOTest {

    @Test
    void checkNoArgsConstructorAndGettersAndSetters() {
        assertThat(ExerciseDTO.class, allOf(hasValidBeanConstructor(),
            hasValidGettersAndSetters()));
    }

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(ExerciseDTO.class)
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .suppress(Warning.STRICT_INHERITANCE)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(ExerciseDTO.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .verify();
    }

}
