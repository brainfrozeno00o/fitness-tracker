package com.pet.fitnesstracker.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class WorkoutExerciseTest {

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(WorkoutExercise.class)
            .usingGetClass()
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .withNonnullFields("id", "repetitions", "intensity", "workout", "exercise")
            .withPrefabValues(Workout.class, createNewWorkout(1L, "Test Workout 1"),
                createNewWorkout(2L, "Test Workout 2"))
            .withPrefabValues(Exercise.class, createNewExercise(1L,"Test Exercise 1"),
                createNewExercise(2L,"Test Exercise 2"))
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(WorkoutExercise.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withPrefabValue(Workout.class, createNewWorkout(1L, "Test Workout 1"))
            .withPrefabValue(Exercise.class, createNewExercise(1L,"Test Exercise 1"))
            .withOnlyTheseFields("id", "repetitions", "intensity")
            .verify();
    }

    @Test
    void checkGettersAndSetters() {
        assertThat(WorkoutExercise.class, allOf(hasValidGettersAndSetters()));
    }

    private Exercise createNewExercise(Long id, String name) {
        Exercise exercise = new Exercise();

        exercise.setId(id);
        exercise.setName(name);

        return exercise;
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
