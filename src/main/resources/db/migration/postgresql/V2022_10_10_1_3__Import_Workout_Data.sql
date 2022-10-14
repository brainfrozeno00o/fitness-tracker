COPY workout (id, trainee_id, name, workout_day) FROM stdin WITH (FORMAT 'csv', DELIMITER ',', HEADER);
"id","trainee_id","name","workout_day"
"1","1","Handsome Workout 1","2022-10-10"
"2","1","Handsome Workout 2","2022-10-12"
"3","1","Handsome Workout 3","2022-10-13"
"4","1","Handsome Workout 4","2022-10-14"
"5","2","Beautiful Workout 1","2022-10-11"
"6","2","Beautiful Workout 2","2022-10-12"
"7","2","Beautiful Workout 3","2022-10-13"
"8","2","Beautiful Workout 4","2022-10-15"
\.