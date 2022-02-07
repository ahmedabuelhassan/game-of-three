package com.takeaway.gameofthree.model;

/**
 * This enum responsible for presenting the event types that sent to web socket client.
 */
public enum EventType {
    JOIN,
    WAITING,
    GAME_OVER,
    DISCONNECT,
    PROCEED,
    TURN,
    VALIDATION,
    WIN
}
