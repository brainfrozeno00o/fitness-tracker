package com.pet.fitnesstracker.domain;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class ExerciseTest {

    @Test
    void checkEqualsAndHashCode() {
        EqualsVerifier.forClass(Exercise.class)
            .usingGetClass()
            .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
            .withNonnullFields("id", "name")
            .withPrefabValues(WorkoutExercise.class, createNewWorkoutExercise(1L,1, "medium"),
                createNewWorkoutExercise(2L, 2, "low"))
            .verify();
    }

    @Test
    void checkGettersAndSettersAndToString() {
        assertThat(Exercise.class, allOf(hasValidGettersAndSetters(),
            hasValidBeanToStringExcluding("workoutExercises")));
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

        Trainee trainee = new Trainee();

        trainee.setId(1L);
        trainee.setName("Test Trainee");
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
