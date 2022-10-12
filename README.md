# Fitness Tracker

This is a personal project using a close friend's idea just to experience building a backend from scratch using the following:
- Java 11
- Spring 2.5.2

This is basically a fitness tracker that:
- Gets the following information
  - Trainee - name, workouts
  - Workout - name and which trainee it belongs to, workout date, exercise/s part of the workout, remarks (if there are)
  - Exercise - name, which workout/s it is part of
  - Workout Exercise - how many reps and intensity, which workout and exercise it belongs to (using names), remarks (if there are)
- Adds the following
  - Trainee - name
  - Workout - name and which trainee it belongs to, workout date, remarks (if there are)
  - Exercise - name
  - Workout Exercise - determining which workout and exercise, and then how many reps and how intense it would be, remarks (if there are)

This service currently uses the _MVC Architecture_ pattern for simplicity. There might be a chance that it would be converted to using Clean Architecture once things go complex.

## TODO
- Delete the following
  - Trainee
  - Workout 
  - Exercise - should probably also update workout exercises as well along the way 
  - Workout Exercise
- Update the following
  - Trainee - name
  - Workout - name and remarks
  - Exercise - name
  - Workout Exercise - intensity, repetitions, and remarks
- Secure the endpoints if possible

## Running the application
```shell
mvn clean && mvn spring-boot:run
```

## API Endpoints

### Retrieve trainee information by ID

```shell
GET /v1/fitness/trainees/{id}
```
<details>
<summary markdown="span">Sample cURL request and response, click to expand</summary>

**Request**

```shell
curl 'http://localhost:8080/v1/fitness/trainees/1'
```

**Success 200 OK Response**

```json
{
    "id": 1,
    "name": "Handsome Trainee",
    "workouts": [
        {
            "id": 1,
            "name": "Handsome Workout 1",
            "workoutDate": "2022-10-10",
            "remarks": null
        },
        // omitted for brevity
    ]
}
```

</details>
<br>

### Retrieve workout information by ID

```shell
GET /v1/fitness/workouts/{id}
```
<details>
<summary markdown="span">Sample cURL request and response, click to expand</summary>

**Request**

```shell
curl 'http://localhost:8080/v1/fitness/workouts/1'
```

**Success 200 OK Response**

```json
{
  "id": 1,
  "name": "Handsome Workout 1",
  "traineeName": "Handsome Trainee",
  "remarks": null,
  "date": "2022-10-10",
  "exercises": [
    {
      "id": 1000,
      "exerciseName": "Exercise 1",
      "repetitions": 10,
      "intensity": "low",
      "remarks": null
    },
    // omitted for brevity
  ]
}
```

</details>
<br>

### Retrieve exercise information by ID

```shell
GET /v1/fitness/exercises/{id}
```
<details>
<summary markdown="span">Sample cURL request and response, click to expand</summary>

**Request**

```shell
curl 'http://localhost:8080/v1/fitness/exercises/1'
```

**Success 200 OK Response**

```json
{
  "id": 1,
  "name": "Exercise 1",
  "workouts": [
    {
      "id": 1,
      "name": "Handsome Workout 1",
      "workoutDate": "2022-10-10",
      "remarks": null
    },
    // omitted for brevity
  ]
}
```

</details>
<br>

### Retrieve workout exercise information by ID

```shell
GET /v1/fitness/workouts/exercises/{id}
```
<details>
<summary markdown="span">Sample cURL request and response, click to expand</summary>

**Request**

```shell
curl 'http://localhost:8080/v1/workouts/exercises/1000'
```

**Success 200 OK Response**

```json
{
  "id": 1000,
  "workoutName": "Handsome Workout 1",
  "exerciseName": "Exercise 1",
  "repetitions": 10,
  "intensity": "low",
  "remarks": null
}
```

</details>
<br>