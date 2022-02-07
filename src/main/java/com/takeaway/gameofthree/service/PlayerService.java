package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.model.Player;

public interface PlayerService {

    Player getPlayerByName(String playerName) throws FunctionalException;

    Player setOnlineStatus(Player player, boolean isOnline);

    void disconnect(String playerName) throws InterruptedException, FunctionalException;
}
