package com.takeaway.gameofthree.exception;

/**
 * This Exception for wrapping any exception occurred
 * in the application to hide the technical details for security purposes.
 */
public class FunctionalException extends Exception {
    public FunctionalException(String message) {
        super(message);
    }
}
