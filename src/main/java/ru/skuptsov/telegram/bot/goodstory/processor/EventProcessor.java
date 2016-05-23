package ru.skuptsov.telegram.bot.goodstory.processor;

import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface EventProcessor {
    void process(UpdateEvent updateEvent);

    boolean isSuitableForProcessingEvent(UpdateEvent updateEvent);
}
