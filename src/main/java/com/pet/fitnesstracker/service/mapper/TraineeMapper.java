package com.pet.fitnesstracker.service.mapper;

import com.pet.fitnesstracker.domain.Trainee;
import com.pet.fitnesstracker.dto.request.AddTraineeRequestDTO;
import com.pet.fitnesstracker.dto.response.TraineeResponseDTO;
import org.mapstruct.Mapper;

/**
 * @author Elmo Lingad
 */
@Mapper(componentModel = "spring")
public interface TraineeMapper {

    TraineeResponseDTO toDto(Trainee trainee);

    Trainee toEntity(AddTraineeRequestDTO addTraineeRequestDTO);

}
