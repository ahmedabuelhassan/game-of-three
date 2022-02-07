package com.takeaway.gameofthree.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This class for representing the data to be retrieved for thymeleaf view.
 */
@Getter
@Setter
@Builder
public class GameDTO {
    private String id;
    private String name;
    private long turn;
    private int currentNumber;
}
