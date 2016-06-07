package ru.skuptsov.telegram.bot.platform.processor;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public interface ConditionEventProcessor extends EventProcessor {

    boolean isSuitableForProcessingEvent(@NotNull UpdateEvent updateEvent);
}
