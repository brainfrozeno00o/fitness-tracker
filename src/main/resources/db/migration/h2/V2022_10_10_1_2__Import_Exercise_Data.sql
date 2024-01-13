INSERT INTO exercise(id, name)
  SELECT * FROM CSVREAD('classpath:db/data/exercise.csv');