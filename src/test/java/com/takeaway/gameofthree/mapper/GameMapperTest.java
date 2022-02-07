package com.takeaway.gameofthree.mapper;

import com.takeaway.gameofthree.dto.GameDTO;
import com.takeaway.gameofthree.model.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.takeaway.gameofthree.constant.GameConstant.MAXIMUM_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GameMapperTest {

    @InjectMocks
    private GameMapperImpl gameMapper;

    @Test
    void whenConvertModelToDTO_shouldDTOReturned() {
        // given
        int currentNumber = ThreadLocalRandom.current().nextInt(3, MAXIMUM_VALUE);
        String id = UUID.randomUUID().toString();
        Game game = buildNewGame(currentNumber, id);
        GameDTO expectedGameDTO = buildNewGameDTO(currentNumber, id);

        // when
        GameDTO actualGameDTO = gameMapper.modelToDTO(game);

        // then
        assertThat(actualGameDTO)
                .usingRecursiveComparison()
                .isEqualTo(expectedGameDTO);
    }

    private GameDTO buildNewGameDTO(int currentNumber, String id) {
        return GameDTO.builder()
                .id(id)
                .name(String.format("Game %d", currentNumber))
                .turn(0)
                .currentNumber(currentNumber)
                .build();
    }

    private Game buildNewGame(int currentNumber, String id) {
        return Game.builder()
                .id(id)
                .name(String.format("Game %d", currentNumber))
                .turn(0)
                .currentNumber(currentNumber)
                .build();
    }


}