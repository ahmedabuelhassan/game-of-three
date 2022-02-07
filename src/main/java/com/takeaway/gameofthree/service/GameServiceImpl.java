package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.dto.GameDTO;
import com.takeaway.gameofthree.dto.RequestDTO;
import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.mapper.GameMapper;
import com.takeaway.gameofthree.model.EventType;
import com.takeaway.gameofthree.model.Game;
import com.takeaway.gameofthree.model.GameStatus;
import com.takeaway.gameofthree.model.Player;
import com.takeaway.gameofthree.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.takeaway.gameofthree.constant.GameConstant.ERROR_MESSAGE;
import static com.takeaway.gameofthree.constant.GameConstant.MAXIMUM_VALUE;

/**
 * This service is responsible for creating and controlling the game.
 */

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final EventSenderService eventSenderService;
    private final PlayerService playerService;

    @Override
    public GameDTO getGameByPlayer(String playerName) throws FunctionalException {
        log.debug("getting game for player: {}", playerName);
        Player player = playerService.getPlayerByName(playerName);
        GameDTO gameDTO = player.getGame() == null ? joinGame(player) : getJoinedGame(player);
        gameDTO.setTurn(player.getId());
        return gameDTO;
    }

    private GameDTO joinGame(Player player) {
        Game availableGame = getAvailableGame();
        checkReadinessOfGame(player, availableGame);
        availableGame.addPlayer(player);
        return gameMapper.modelToDTO(gameRepository.save(availableGame));
    }

    private void checkReadinessOfGame(Player player, Game availableGame) {
        // check if any player to joined to this game or not.
        if (!availableGame.getPlayers().isEmpty()) {
            eventSenderService.buildSendEvent(availableGame, player.getName(), EventType.JOIN);
            availableGame.setGameStatus(GameStatus.GAME_IN_PROGRESS);
            String opponentPlayName = availableGame.getPlayers().stream().findFirst().get().getName();
            eventSenderService.buildSendEvent(availableGame, opponentPlayName, EventType.PROCEED);
        } else {
            eventSenderService.buildSendEvent(availableGame, player.getName(), EventType.WAITING);
            availableGame.setTurn(player.getId());
        }
    }

    private Game getAvailableGame() {
        Optional<Game> lastAvailableGame = gameRepository.findTopByGameStatusOrderByIdDesc(GameStatus.GAME_NOT_STARTED);
        return lastAvailableGame.orElseGet(this::buildNewGame);
    }

    private GameDTO getJoinedGame(Player player) {
        return gameMapper.modelToDTO(player.getGame());
    }

    @Override
    public void play(RequestDTO requestDTO, String playerName) throws FunctionalException {
        if (!validNumber(requestDTO.getAddedValue())) {
            throw new FunctionalException(ERROR_MESSAGE);
        }
        Player player = playerService.getPlayerByName(playerName);
        Game game = player.getGame();
        if (game.getGameStatus() == GameStatus.GAME_OVER) {
            eventSenderService.buildSendEvent(game, playerName, EventType.GAME_OVER);
            return;
        }
        if (game.getTurn() != player.getId()) {
            eventSenderService.buildSendEvent(game, playerName, EventType.TURN);
            return;
        }
        Set<Player> gamePlayers = game.getPlayers();
        if (gamePlayers.size() == 1) {
            eventSenderService.buildSendEvent(game, playerName, EventType.WAITING);
            return;
        }
        int result = requestDTO.getAddedValue() + game.getCurrentNumber();
        boolean isDivisible = result % 3 == 0;
        if (result == 3) {
            game.setCurrentNumber(1);
            game.setGameStatus(GameStatus.GAME_OVER);
            eventSenderService.buildPlayEvent(game, playerName, requestDTO.getAddedValue(), true);
            eventSenderService.buildSendEvent(game, playerName, EventType.WIN);

        } else {
            if (isDivisible) {
                game.setCurrentNumber(result / 3);
            }
            game.setTurn(gamePlayers.stream().filter(p -> p.getId() != player.getId()).findFirst().get().getId());
            eventSenderService.buildPlayEvent(game, playerName, requestDTO.getAddedValue(), isDivisible);
        }
        gameRepository.save(game);
    }

    private boolean validNumber(int addedValue) {
        return addedValue == -1 || addedValue == 0 || addedValue == 1;
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
