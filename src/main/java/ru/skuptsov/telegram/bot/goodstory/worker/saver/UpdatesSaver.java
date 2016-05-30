package ru.skuptsov.telegram.bot.goodstory.worker.saver;

import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvents;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
public interface UpdatesSaver {
    void save(@NotNull UpdateEvents updateEvents);
}
