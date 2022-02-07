package com.takeaway.gameofthree.controller;

import com.takeaway.gameofthree.dto.GameDTO;
import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.takeaway.gameofthree.constant.GameConstant.MAXIMUM_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class GameRestBasedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    @WithMockUser("player1")
    void whenGetAvailableGame_shouldGameDataRetrieved() throws Exception {
        // given
        GameDTO gameDTO = buildNewGameDTO();
        Mockito.when(gameService.getGameByPlayer("player1")).thenReturn(gameDTO);

        //when
        ResultActions resultActions = this.mockMvc.perform(get("/"));


        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("game", gameDTO))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser("player1")
    void should_redirect_to_error_page_when_exception_thrown() throws Exception {
        // given
        Mockito.when(gameService.getGameByPlayer("player1")).thenThrow(new FunctionalException("user player1 not found"));

        //when
        ResultActions resultActions = this.mockMvc.perform(get("/"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }


    private GameDTO buildNewGameDTO() {
        int currentNumber = ThreadLocalRandom.current().nextInt(3, MAXIMUM_VALUE);

        return GameDTO.builder()
                .id(UUID.randomUUID().toString())
                .name(String.format("Game %d", currentNumber))
                .turn(0)
                .currentNumber(currentNumber)
                .build();
    }

}