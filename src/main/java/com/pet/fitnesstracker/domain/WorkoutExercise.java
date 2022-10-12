package com.pet.fitnesstracker.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Elmo Lingad
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class WorkoutExercise {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "repetitions", nullable = false)
    private int repetitions;

    @Column(name = "intensity", nullable = false)
    private String intensity;

    private String remarks;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkoutExercise that = (WorkoutExercise) o;

        if (!getId().equals(that.getId())) {
            return false;
        }

        if (!getWorkout().getName().equals(that.getWorkout().getName())) {
            return false;
        }

        return getExercise().getName().equals(that.getExercise().getName());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getWorkout().getName().hashCode() + getExercise().getName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Workout Exercise {" +
            "id=" + id +
            ", exercise='" + exercise.getName() + "'" +
            ", date='" + workout.getWorkoutDate() + "'" +
            ", repetitions=" + repetitions +
            ", intensity='" + intensity + "'" +
            "}";
    }

}
