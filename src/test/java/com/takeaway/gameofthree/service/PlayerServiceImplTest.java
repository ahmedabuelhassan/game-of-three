package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.model.EventType;
import com.takeaway.gameofthree.model.Game;
import com.takeaway.gameofthree.model.GameStatus;
import com.takeaway.gameofthree.model.Player;
import com.takeaway.gameofthree.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.takeaway.gameofthree.constant.GameConstant.MAXIMUM_VALUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private EventSenderService eventSenderService;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void whenGetPlayerByName_shouldReturnPlayer_ifFound() {
        // given
        String playerName = "player1";
        Player expectedPlayer = buildPlayer(playerName);
        when(playerRepository.findByName(playerName)).thenReturn(Optional.ofNullable(expectedPlayer));

        // when
        Player actualPlayer = assertDoesNotThrow(() -> playerService.getPlayerByName(playerName));


        //then
        assertNotNull(expectedPlayer);
        assertEquals(expectedPlayer.getName(), actualPlayer.getName());
        assertEquals(expectedPlayer.isOnline(), actualPlayer.isOnline());
        verify(playerRepository).findByName(playerName);
    }

    // asserts that the global exception handler is working fine.
    @Test
    void should_throw_exception_when_user_doesnt_exist() {
        // given
        String playerName = "player1";
        when(playerRepository.findByName(playerName)).thenReturn(Optional.empty());

        // when
        FunctionalException functionalException = assertThrows(FunctionalException.class, () -> playerService.getPlayerByName(playerName));

        //then
        assertEquals(String.format("player %s not found", playerName), functionalException.getMessage());
        verify(playerRepository).findByName(playerName);
    }

    @Test
    void whenSetOnlineStatus_ShouldStatusUpdated() {
        // given
        Player offlinePlayer = buildPlayer("player1");
        offlinePlayer.setOnline(false);

        Player onlinePlayer = buildPlayer("player1");
        onlinePlayer.setOnline(true);

        when(playerRepository.save(any(Player.class))).thenReturn(onlinePlayer);

        // when
        Player savedPlayer = playerService.setOnlineStatus(offlinePlayer, true);

        // then
        assertTrue(savedPlayer.isOnline());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void whenPlayerDisconnect_shouldDisconnectEventTriggered() {
        // given
        String playerName = "player1";
        Player expectedPlayer = buildPlayer(playerName);
        Game game = buildNewGame();
        expectedPlayer.setGame(game);
        when(playerRepository.findByName(playerName)).thenReturn(Optional.of(expectedPlayer));

        // when
        assertDoesNotThrow(() -> playerService.disconnect(playerName));

        // then
        verify(eventSenderService).buildSendEvent(game, playerName, EventType.DISCONNECT);
    }

    private Player buildPlayer(String playerName) {
        return Player.builder().name(playerName).build();
    }

    private Game buildNewGame() {
        int currentNumber = ThreadLocalRandom.current().nextInt(3, MAXIMUM_VALUE);
        return Game.builder()
                .id(UUID.randomUUID().toString())
                .name(String.format("Game %d", currentNumber))
                .currentNumber(currentNumber)
                .gameStatus(GameStatus.GAME_NOT_STARTED)
                .players(new LinkedHashSet<>())
                .build();
    }
}