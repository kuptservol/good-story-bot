package ru.skuptsov.telegram.bot.platform.processor.resolver;

import org.telegram.telegrambots.api.objects.Message;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public class CommandEventProcessorResolver extends AbstractProcessorResolver {

    private final Map<String, EventProcessor> commandEventProcessorMap;

    public CommandEventProcessorResolver(@NotNull Map<String, EventProcessor> commandEventProcessorMap) {
        this.commandEventProcessorMap = commandEventProcessorMap;
    }

    @Override
    protected EventProcessor resolveProcessor(UpdateEvent updateEvent) {
        return ofNullable(commandEventProcessorMap.get(getMessageText(updateEvent)))
                .orElse(EventProcessor.EMPTY);
    }

    private String getMessageText(UpdateEvent updateEvent) {
        return ofNullable(updateEvent.getUpdate().getMessage())
                .map(Message::getText)
                .orElse("");
    }
}
