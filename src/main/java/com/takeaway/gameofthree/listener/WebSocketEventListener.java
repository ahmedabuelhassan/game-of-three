package com.takeaway.gameofthree.listener;

import com.takeaway.gameofthree.model.Player;
import com.takeaway.gameofthree.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * This class for listening the events that triggered by the web socket client.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final PlayerService playerService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionConnectEvent event) {
        try {
            Principal user = event.getUser();
            if (user != null) {
                String playerName = user.getName();
                log.debug("Player {} connected", playerName);
                Player player = playerService.getPlayerByName(playerName);
                playerService.setOnlineStatus(player, true);
            }
        } catch (Exception e) {
            log.error("an error occurred while triggering connect event", e);
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        try {
            Principal user = event.getUser();
            if (user != null) {
                String playerName = user.getName();
                log.debug("Player {} disconnected", playerName);
                Player player = playerService.getPlayerByName(playerName);
                playerService.setOnlineStatus(player, false);
                playerService.disconnect(player.getName());
            }
        } catch (InterruptedException e) {
            log.warn("Session Interrupted!", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("an error occurred while triggering disconnect event", e);
        }
    }

}
