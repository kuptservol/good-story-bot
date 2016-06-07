package ru.skuptsov.telegram.bot.platform.processor.resolver;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public interface ProcessorResolver {
    EventProcessor resolve(@NotNull UpdateEvent updateEvent);

    ProcessorResolver setNext(ProcessorResolver processorResolver);
}
