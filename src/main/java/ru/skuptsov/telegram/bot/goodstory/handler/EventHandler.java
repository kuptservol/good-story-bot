package ru.skuptsov.telegram.bot.goodstory.handler;

import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface EventHandler {

    void handleEvent(@NotNull UpdateEvent updateEvent);
}
