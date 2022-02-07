package com.takeaway.gameofthree.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * This class for representing the data that sent from web socket client.
 */
@Getter
@Setter
public class RequestDTO {
    private int addedValue;
}
