CREATE SEQUENCE hibernate_sequence START 1000 INCREMENT 1;

CREATE TABLE trainee (
  id INT8 NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL CONSTRAINT unq_trainee_name UNIQUE
);

CREATE TABLE workout (
  id INT8 NOT NULL PRIMARY KEY,
  trainee_id INT8 NOT NULL,
  name VARCHAR NOT NULL,
  workout_day VARCHAR NOT NULL,
  remarks VARCHAR,
  FOREIGN KEY (trainee_id) REFERENCES trainee (id)
);

CREATE TABLE exercise (
  id INT8 NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL CONSTRAINT unq_exercise_name UNIQUE
);

CREATE TABLE workout_exercise (
  id INT8 NOT NULL PRIMARY KEY DEFAULT nextval('hibernate_sequence'),
  workout_id INT8 NOT NULL,
  exercise_id INT8 NOT NULL,
  repetitions INT NOT NULL,
  intensity VARCHAR NOT NULL,
  remarks VARCHAR,
  FOREIGN KEY (workout_id) REFERENCES workout (id),
  FOREIGN KEY (exercise_id) REFERENCES exercise (id)
);