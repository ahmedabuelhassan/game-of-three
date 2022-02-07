package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.model.EventType;
import com.takeaway.gameofthree.model.Game;
import com.takeaway.gameofthree.model.Player;
import com.takeaway.gameofthree.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * This service for getting the player data from DB and update it if needed.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final EventSenderService eventSenderService;
    private final PlayerRepository playerRepository;

    @Override
    public Player getPlayerByName(String playerName) throws FunctionalException {
        return playerRepository.findByName(playerName).orElseThrow(() -> new FunctionalException(String.format("player %s not found", playerName)));
    }

    @Override
    public Player setOnlineStatus(Player player, boolean isOnline) {
        player.setOnline(isOnline);
        return playerRepository.save(player);
    }

    @Override
    @Async
    public void disconnect(String playerName) throws InterruptedException, FunctionalException {
        // delaying for handing refresh page and websocket re-connect before pushing to make sure that the player actually disconnected
        Thread.sleep(3000);
        Player player = getPlayerByName(playerName);
        Game game = player.getGame();
        if (!player.isOnline()) {
            eventSenderService.buildSendEvent(game, playerName, EventType.DISCONNECT);
        }
    }

}
