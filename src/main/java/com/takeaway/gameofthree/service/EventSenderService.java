package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.model.EventType;
import com.takeaway.gameofthree.model.Game;
import org.springframework.scheduling.annotation.Async;

public interface EventSenderService {
    @Async
    void buildSendEvent(Game game, String playerName, EventType eventType);

    void buildPlayEvent(Game game, String playerName, int addedValue, boolean isDivisible);
}
