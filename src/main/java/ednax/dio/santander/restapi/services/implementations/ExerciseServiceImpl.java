package ednax.dio.santander.restapi.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ednax.dio.santander.restapi.dtos.request.ExerciseRequestDTO;
import ednax.dio.santander.restapi.dtos.response.ExerciseResponseDTO;
import ednax.dio.santander.restapi.exceptions.RestException;
import ednax.dio.santander.restapi.models.ExerciseModel;
import ednax.dio.santander.restapi.models.WorkoutModel;
import ednax.dio.santander.restapi.repositories.ExerciseRepository;
import ednax.dio.santander.restapi.repositories.WorkoutRepository;
import ednax.dio.santander.restapi.services.ExerciseService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    
    private final ExerciseRepository repository;
    private final WorkoutRepository workoutRepository;
    private final ModelMapper modelMapper;

    @Override
    public ExerciseResponseDTO create(ExerciseRequestDTO request) {
        ExerciseModel exerciseToSave = modelMapper.map(request, ExerciseModel.class);
        
        WorkoutModel workout = workoutRepository.findById(WorkoutServiceImpl.validateWorkoutId(request.getWorkout()))
            .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, String.format("The Workout with id %s does not exists.", request.getWorkout()))
        );

        ExerciseModel savedExercise = repository.save(exerciseToSave);
        workout.getExercises().add(savedExercise);
        workoutRepository.save(workout);

        return modelMapper.map(savedExercise, ExerciseResponseDTO.class);
    }

    @Override
    public void delete(String id) {
        var longId = validateExerciseId(id);

        if(!repository.findById(longId).isPresent()) throw new RestException(HttpStatus.NOT_FOUND, String.format("The Exercise with id %s does not exists.", id));
        
        repository.deleteById(longId);
    }

    @Override
    public List<ExerciseResponseDTO> findAll() {
        List<ExerciseModel> exerciseModels = repository.findAll();
        List<ExerciseResponseDTO> response = new ArrayList<>();

        exerciseModels.forEach(exercise -> {
            response.add(modelMapper.map(exercise, ExerciseResponseDTO.class));
        });

        return response;
    }

    @Override
    public ExerciseResponseDTO findById(String id) {
        var longId = validateExerciseId(id);

        ExerciseModel exercise = repository.findById(longId).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, String.format("The Exercise with id %s does not exists.", id)));

        return modelMapper.map(exercise, ExerciseResponseDTO.class);
    }

    @Override
    public ExerciseResponseDTO update(String id, ExerciseRequestDTO request) {
        var longId = validateExerciseId(id);

        if(!repository.findById(longId).isPresent()) throw new RestException(HttpStatus.NOT_FOUND, String.format("The Exercise with id %s does not exists.", id));

        ExerciseModel exerciseToModify = modelMapper.map(request, ExerciseModel.class);
        exerciseToModify.setId(longId);
        ExerciseModel modifiedExercise = repository.save(exerciseToModify);
        ExerciseResponseDTO respose = modelMapper.map(modifiedExercise, ExerciseResponseDTO.class);

        return respose;
    }

    
    Long validateExerciseId(String id) {
        try {
            return Long.parseLong(id);
        }
        catch (Exception e) {
            throw new RestException(HttpStatus.BAD_REQUEST, String.format("%s is not a valid Long", id));
        }
    }

}
