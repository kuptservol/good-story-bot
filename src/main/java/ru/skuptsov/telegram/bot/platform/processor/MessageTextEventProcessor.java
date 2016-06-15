package ru.skuptsov.telegram.bot.platform.processor;

import org.telegram.telegrambots.api.objects.Update;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public interface MessageTextEventProcessor extends EventProcessor {

    @NotNull
    List<String> getMessageText();
}
