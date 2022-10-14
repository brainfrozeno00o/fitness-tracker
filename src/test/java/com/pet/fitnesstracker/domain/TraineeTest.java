package com.pet.fitnesstracker.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class TraineeTest {

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(Trainee.class)
            .usingGetClass()
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .withNonnullFields("id", "name")
            .withPrefabValues(Workout.class, createNewWorkout(1L, "Test Workout 1"),
                createNewWorkout(2L, "Test Workout 2"))
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(Trainee.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withPrefabValue(Workout.class, createNewWorkout(1L, "Test Workout 1"))
            .withOnlyTheseFields("id", "name")
            .verify();
    }

    @Test
    void checkGettersAndSetters() {
        assertThat(Trainee.class, allOf(hasValidGettersAndSetters()));
    }

    @Test
    void checkManipulatingWorkouts() {
        Trainee trainee = new Trainee();

        assertTrue(trainee.getWorkouts().isEmpty());

        Workout workout = createNewWorkout(1L, "Test Workout 1");

        trainee.addWorkout(workout);

        assertFalse(trainee.getWorkouts().isEmpty());
        assertEquals(1, trainee.getWorkouts().size());
        assertEquals(trainee, workout.getTrainee());

        trainee.removeWorkout(workout);

        assertTrue(trainee.getWorkouts().isEmpty());
        assertNull(workout.getTrainee());
    }

    private Workout createNewWorkout(Long id, String name) {
        Workout workout = new Workout();

        workout.setId(id);
        workout.setName(name);
        workout.setWorkoutDate("2022-10-12");
        workout.setRemarks("Test Remarks");

        Trainee trainee = new Trainee();

        trainee.setId(id);
        trainee.setName("Test Trainee for " + name);
        trainee.setWorkouts(Collections.singletonList(workout));

        workout.setTrainee(trainee);

        return workout;
    }

}
