package ru.skuptsov.telegram.bot.platform.processor.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.DefaultEventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.annotation.Nullable;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public class DefaultEventProcessorResolver extends AbstractProcessorResolver {
    private final Logger log = LoggerFactory.getLogger(DefaultEventProcessorResolver.class);

    private final DefaultEventProcessor defaultEventProcessor;

    public DefaultEventProcessorResolver(@Nullable DefaultEventProcessor defaultEventProcessor) {
        if (defaultEventProcessor == null) {
            defaultEventProcessor = updateEvent -> {
                log.info("No suitable processor found for event {}", updateEvent);
                return ApiCommand.EMPTY;
            };
        }
        this.defaultEventProcessor = defaultEventProcessor;
    }

    @Override
    protected EventProcessor resolveProcessor(UpdateEvent updateEvent) {
        return defaultEventProcessor;
    }
}
