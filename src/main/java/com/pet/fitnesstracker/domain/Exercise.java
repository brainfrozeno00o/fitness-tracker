package com.pet.fitnesstracker.domain;

import java.util.List;
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
public class Exercise {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Exercise that = (Exercise) o;

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
        return "Exercise {" +
            "id=" + id +
            ", name='" + name +
            "'}";
    }

}
