package com.takeaway.gameofthree.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class for handling any global exception then redirect the request to error page.
 */

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex) {
        log.error("an exception handled, then redirect to error page", ex);
        return "error";
    }
}
