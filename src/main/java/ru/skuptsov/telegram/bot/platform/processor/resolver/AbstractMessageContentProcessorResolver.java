package ru.skuptsov.telegram.bot.platform.processor.resolver;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public abstract class AbstractMessageContentProcessorResolver extends AbstractProcessorResolver {

    private final Map<Object, EventProcessor> messageContentProcessorMap;

    public AbstractMessageContentProcessorResolver(@NotNull Map<Object, EventProcessor> messageContentProcessorMap) {
        this.messageContentProcessorMap = messageContentProcessorMap;
    }

    @Override
    protected EventProcessor resolveProcessor(UpdateEvent updateEvent) {
        return ofNullable(messageContentProcessorMap.get(getMessageContent(updateEvent)))
                .orElse(EventProcessor.EMPTY);
    }

    protected abstract Object getMessageContent(UpdateEvent updateEvent);
}
