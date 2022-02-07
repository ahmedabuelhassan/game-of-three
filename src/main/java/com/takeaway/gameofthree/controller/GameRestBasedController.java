package com.takeaway.gameofthree.controller;

import com.takeaway.gameofthree.exception.FunctionalException;
import com.takeaway.gameofthree.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * This controller for handling the home page and responsible for
 * propagating the required data for establishing web socket later.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class GameRestBasedController {

    private final GameService gameService;

    @GetMapping("/")
    public String getAvailableGame(Principal principal, Model model) throws FunctionalException {
        String playerName = principal.getName();
        log.debug("player {} logged in successfully and trying to join an available game", playerName);
        model.addAttribute("game", gameService.getGameByPlayer(playerName));
        return "index";
    }
}
