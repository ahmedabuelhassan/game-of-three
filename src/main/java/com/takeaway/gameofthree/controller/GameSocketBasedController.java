package com.takeaway.gameofthree.controller;

import com.takeaway.gameofthree.dto.RequestDTO;
import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.takeaway.gameofthree.constant.GameConstant.ERROR_MESSAGE;

/**
 * This controller for handling the web socket communications.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class GameSocketBasedController {

    private final GameService gameService;

    @MessageMapping("/play")
    public void play(RequestDTO requestDTO, Principal principal) throws FunctionalException {
        String playerName = principal.getName();
        log.debug("player {} playing and adding {}", playerName, requestDTO.getAddedValue());
        gameService.play(requestDTO, playerName);
    }

    // wrapping the technical exception in another exception for security purposes (hiding the used technologies and so on)
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable e) {
        log.error("an exception occurred:", e);
        return ERROR_MESSAGE;
    }
}
