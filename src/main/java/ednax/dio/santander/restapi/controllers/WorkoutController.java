package ednax.dio.santander.restapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ednax.dio.santander.restapi.dtos.request.ExerciseRequestDTO;
import ednax.dio.santander.restapi.dtos.request.WorkoutRequestDTO;
import ednax.dio.santander.restapi.dtos.response.ExerciseResponseDTO;
import ednax.dio.santander.restapi.dtos.response.WorkoutResponseDTO;
import ednax.dio.santander.restapi.services.ExerciseService;
import ednax.dio.santander.restapi.services.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    @GetMapping
    ResponseEntity<List<WorkoutResponseDTO>> getAll() {
        var response = workoutService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<WorkoutResponseDTO> getById(@PathVariable String id) {
        var response = workoutService.findById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        workoutService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<WorkoutResponseDTO> update(@PathVariable String id, @RequestBody @Valid WorkoutRequestDTO request) {
        var response = workoutService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exercises")
    ResponseEntity<List<ExerciseResponseDTO>> getWorkoutsExercises(@PathVariable String id) {
        var response = workoutService.findWorkoutsExercises(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/exercises")
    ResponseEntity<ExerciseResponseDTO> createWorkoutsExercise(@PathVariable String id, @RequestBody @Valid ExerciseRequestDTO request) {
        var response = exerciseService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
