package ru.skuptsov.telegram.bot.platform.processor;

import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface EventProcessor {
    EventProcessor EMPTY = (updateEvent) -> ApiCommand.EMPTY;

    ApiCommand process(UpdateEvent updateEvent);
}
