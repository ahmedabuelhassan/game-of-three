package com.takeaway.gameofthree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class for representing the data to be retrieved for web socket client.
 */
@Getter
@Setter
@ToString
@Builder
public class ResponseDTO {

    private int currentNumber;
    private long turn;
    private boolean autoPlay;
    private boolean playAgain;
    private String event;

}
