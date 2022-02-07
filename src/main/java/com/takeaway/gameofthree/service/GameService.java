package com.takeaway.gameofthree.service;

import com.takeaway.gameofthree.dto.GameDTO;
import com.takeaway.gameofthree.dto.RequestDTO;
import com.takeaway.gameofthree.exception.FunctionalException;

public interface GameService {

    GameDTO getGameByPlayer(String playerName) throws FunctionalException;

    void play(RequestDTO requestDTO, String playerName) throws FunctionalException;
}
