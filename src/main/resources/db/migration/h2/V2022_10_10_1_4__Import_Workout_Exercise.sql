INSERT INTO workout_exercise(workout_id, exercise_id, repetitions, intensity)
  SELECT * FROM CSVREAD('classpath:db/data/map_workout_exercise.csv')