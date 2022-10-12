INSERT INTO trainee(id, name)
  SELECT * FROM CSVREAD('classpath:db/data/trainee.csv');