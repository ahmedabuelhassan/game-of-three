package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.dto.ResponseDTO;
import com.takeaway.gameofthree.model.EventType;
import com.takeaway.gameofthree.model.Game;
import com.takeaway.gameofthree.model.GameStatus;
import com.takeaway.gameofthree.util.GameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.takeaway.gameofthree.constant.GameConstant.GAME_TOPIC;

/**
 * This service is responsible for sending the messages to web socket client.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EventSenderServiceImpl implements EventSenderService {

    private final GameUtil gameUtil;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @Override
    public void buildSendEvent(Game game, String playerName, EventType eventType) {
        log.debug("pushing an event {} for game: {}", eventType, game.getId());
        try {
            Optional<String> eventMessage = Optional.empty();
            switch (eventType) {
                case JOIN:
                    Thread.sleep(1000);
                    eventMessage = Optional.ofNullable(gameUtil.getKey("JOIN-MSG", playerName));
                    break;
                case WAITING:
                    Thread.sleep(2000);
                    eventMessage = Optional.ofNullable(gameUtil.getKey("WAITING-MSG"));
                    break;
                case GAME_OVER:
                    eventMessage = Optional.ofNullable(gameUtil.getKey("GAME-OVER-MSG"));
                    break;
                case DISCONNECT:
                    eventMessage = Optional.ofNullable(gameUtil.getKey("DISCONNECT-MSG", playerName));
                    break;
                case PROCEED:
                    Thread.sleep(2000);
                    eventMessage = Optional.ofNullable(gameUtil.getKey("PROCEED-MSG", playerName));
                    break;
                case TURN:
                    eventMessage = Optional.ofNullable(gameUtil.getKey("TURN-MSG", playerName));
                    break;
                case VALIDATION:
                    eventMessage = Optional.ofNullable(gameUtil.getKey("VALIDATION-MSG", playerName));
                    break;
                case WIN:
                    eventMessage = Optional.ofNullable(gameUtil.getKey("WIN-MSG", playerName));
                    break;
                default:
                    break;
            }
            send(game, eventMessage);
        } catch (InterruptedException e) {
            log.warn(" Session Interrupted!", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("an error occurred while pushing an event", e);
        }
    }

    @Override
    public void buildPlayEvent(Game game, String playerName, int addedValue, boolean isDivisible) {
        String key = isDivisible ? "PLAY-DIVISIBLE-MSG" : "PLAY-NOT-DIVISIBLE-MSG";
        String message = gameUtil.getKey(key, playerName, addedValue, game.getCurrentNumber());
        send(game, Optional.ofNullable(message));
    }

    private void send(Game game, Optional<String> eventMessage) {
        eventMessage.ifPresent(event -> {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .autoPlay(game.getGameStatus() == GameStatus.GAME_IN_PROGRESS)
                    .playAgain(game.getGameStatus() == GameStatus.GAME_OVER)
                    .event(event)
                    .currentNumber(game.getCurrentNumber())
                    .turn(game.getTurn())
                    .build();
            this.messagingTemplate.convertAndSend(GAME_TOPIC + game.getId(), responseDTO);
        });
    }

}
