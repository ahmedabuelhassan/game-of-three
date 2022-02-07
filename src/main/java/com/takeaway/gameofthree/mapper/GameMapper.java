package com.takeaway.gameofthree.mapper;

import com.takeaway.gameofthree.dto.GameDTO;
import com.takeaway.gameofthree.model.Game;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

/**
 * This class is responsible for converting the business model to DTO.
 */
@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface GameMapper {

    GameDTO modelToDTO(Game game);

}
