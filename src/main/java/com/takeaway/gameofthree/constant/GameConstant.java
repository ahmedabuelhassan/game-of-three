package com.takeaway.gameofthree.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * This class for presenting the constants of the application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameConstant {
    public static final String GAME_TOPIC = "/topic/game/";
    public static final int MAXIMUM_VALUE = 5000;
    public static final String ERROR_MESSAGE = "please add a valid number!";
}
