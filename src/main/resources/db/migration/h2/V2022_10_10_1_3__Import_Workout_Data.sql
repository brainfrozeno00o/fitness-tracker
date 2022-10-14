INSERT INTO workout(id, trainee_id, name, workout_day)
  SELECT * FROM CSVREAD('classpath:db/data/workout.csv')