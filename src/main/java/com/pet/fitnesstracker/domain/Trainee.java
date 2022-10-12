package com.pet.fitnesstracker.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Trainee {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(
        mappedBy = "trainee",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Workout> workouts = new ArrayList<>();

    // see https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
    public void addWorkout(Workout workout) {
        workouts.add(workout);
        workout.setTrainee(this);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
        workout.setTrainee(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Trainee that = (Trainee) o;

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
        return "Trainee {" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", workouts:" + workouts.stream().map(Workout::getName).collect(Collectors.toList()) +
            "}";
    }

}
