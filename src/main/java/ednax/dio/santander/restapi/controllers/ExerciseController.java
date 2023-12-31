package ednax.dio.santander.restapi.controllers;

import java.util.List;

import ednax.dio.santander.restapi.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ednax.dio.santander.restapi.dtos.response.ExerciseResponseDTO;
import ednax.dio.santander.restapi.services.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private final ExerciseService exerciseService;

    @GetMapping
    ResponseEntity<List<ExerciseResponseDTO>> getAll() {
        var response = exerciseService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<ExerciseResponseDTO> create(@RequestBody @Valid ExerciseRequestDTO request) {
        var response = exerciseService.create(request);
        logger.info(String.format("Exercise %s created.", response.getId().toString()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<ExerciseResponseDTO> getById(@PathVariable String id) {
        var response = exerciseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        exerciseService.delete(id);
        logger.info(String.format("Exercise %s deleted", id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<ExerciseResponseDTO> update(@PathVariable String id, @RequestBody @Valid ExerciseRequestDTO request) {
        var response = exerciseService.update(id, request);
        logger.info(String.format("Exercise %s updated.", id));
        return ResponseEntity.ok(response);
    }
    
}
