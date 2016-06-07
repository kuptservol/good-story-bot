package ru.skuptsov.telegram.bot.platform.processor.resolver;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public abstract class AbstractProcessorResolver implements ProcessorResolver {

    private ProcessorResolver nextAbstractProcessorResolver;

    @Override
    public EventProcessor resolve(@NotNull UpdateEvent updateEvent) {
        EventProcessor eventProcessor = resolveProcessor(updateEvent);
        if (eventProcessor == EventProcessor.EMPTY && nextAbstractProcessorResolver != null) {
            return nextAbstractProcessorResolver.resolve(updateEvent);
        }

        return eventProcessor;
    }

    @Override
    public ProcessorResolver setNext(@NotNull ProcessorResolver processorResolver) {
        this.nextAbstractProcessorResolver = processorResolver;

        return processorResolver;
    }

    protected abstract EventProcessor resolveProcessor(UpdateEvent updateEvent);
}
