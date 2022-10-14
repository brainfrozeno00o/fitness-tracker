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
class WorkoutTest {

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(Workout.class)
            .usingGetClass()
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .withNonnullFields("id", "name", "workoutDate")
            .withPrefabValues(Trainee.class, createNewTrainee(1L, "Test Trainee 1"),
                createNewTrainee(2L, "Test Trainee 2"))
            .withPrefabValues(WorkoutExercise.class, createNewWorkoutExercise(1L,1, "medium"),
                createNewWorkoutExercise(2L, 2, "low"))
            .verify();
    }

    @Test
    void checkToString() {
        ToStringVerifier.forClass(Workout.class)
            .withClassName(NameStyle.SIMPLE_NAME)
            .withPrefabValue(WorkoutExercise.class, createNewWorkoutExercise(1L,1, "medium"))
            .withOnlyTheseFields("id", "name", "workoutDate")
            .verify();
    }

    @Test
    void checkGettersAndSetters() {
        assertThat(Workout.class, allOf(hasValidGettersAndSetters()));
    }

    private Trainee createNewTrainee(Long id, String name) {
        Trainee trainee = new Trainee();

        trainee.setId(id);
        trainee.setName(name);

        return trainee;
    }

    private Exercise createNewExercise() {
        Exercise exercise = new Exercise();

        exercise.setId(1L);
        exercise.setName("Test Trainee");

        return exercise;
    }

    private Workout createNewWorkout() {
        Workout workout = new Workout();

        workout.setId(1L);
        workout.setName("Test Workout");
        workout.setWorkoutDate("2022-10-12");
        workout.setRemarks("Test Remarks");

        Trainee trainee = createNewTrainee(1L, "Test Trainee 1");

        trainee.setWorkouts(Collections.singletonList(workout));

        workout.setTrainee(trainee);

        return workout;
    }

    private WorkoutExercise createNewWorkoutExercise(Long id, int repetitions, String intensity) {
        Workout workout = createNewWorkout();
        Exercise exercise = createNewExercise();
        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setId(id);
        workoutExercise.setRepetitions(repetitions);
        workoutExercise.setIntensity(intensity);
        workoutExercise.setRemarks("Test Remarks");
        workoutExercise.setWorkout(workout);

        exercise.setWorkoutExercises(Collections.singletonList(workoutExercise));

        workoutExercise.setExercise(exercise);

        return workoutExercise;
    }

}
