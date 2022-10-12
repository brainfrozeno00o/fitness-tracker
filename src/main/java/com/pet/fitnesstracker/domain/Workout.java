package com.pet.fitnesstracker.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Workout {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "workout_day", nullable = false)
    private String workoutDate;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trainee trainee;

    @OneToMany(mappedBy = "workout")
    private List<WorkoutExercise> workoutExercises;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Workout that = (Workout) o;

        if (!getId().equals(that.getId())) {
            return false;
        }
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Workout {" +
            "id=" + id +
            ", name='" + name + "'" +
            ", workoutDate='" + workoutDate + "'" +
            ", trainee='" + trainee.getName() + "'" +
            "}";
    }

}
