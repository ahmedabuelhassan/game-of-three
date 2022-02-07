package com.takeaway.gameofthree.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * This util for getting the messages that should be sent to web socket client.
 */

@Component
@RequiredArgsConstructor
public class GameUtil {

    private final ResourceBundleMessageSource messageSource;

    public String getKey(String key) {
        return messageSource.getMessage(key, null, Locale.ENGLISH);
    }

    public String getKey(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.ENGLISH);
    }

}
