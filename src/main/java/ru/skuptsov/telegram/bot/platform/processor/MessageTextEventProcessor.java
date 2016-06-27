package ru.skuptsov.telegram.bot.platform.processor;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public interface MessageTextEventProcessor extends EventProcessor {

    @NotNull
    Set<String> getMessageText();
}
