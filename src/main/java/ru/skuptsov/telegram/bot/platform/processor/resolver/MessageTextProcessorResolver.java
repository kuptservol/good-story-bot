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
public class MessageTextProcessorResolver extends AbstractMessageContentProcessorResolver {

    public MessageTextProcessorResolver(@NotNull Map<Object, EventProcessor> messageContentProcessorMap) {
        super(messageContentProcessorMap);
    }

    @Override
    protected Object getMessageContent(UpdateEvent updateEvent) {
        return ofNullable(updateEvent.getUpdate().getMessage())
                .map(Message::getText)
                .orElse("");
    }
}
