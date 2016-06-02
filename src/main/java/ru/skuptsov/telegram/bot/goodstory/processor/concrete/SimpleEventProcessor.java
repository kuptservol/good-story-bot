package ru.skuptsov.telegram.bot.goodstory.processor.concrete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;
import ru.skuptsov.telegram.bot.goodstory.processor.EventProcessor;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Component
public class SimpleEventProcessor implements EventProcessor {
    private final Logger log = LoggerFactory.getLogger(SimpleEventProcessor.class);

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {
        log.debug("Processing event {}", updateEvent);
        return ApiCommand.EMPTY;
    }

    @Override
    public boolean isSuitableForProcessingEvent(UpdateEvent updateEvent) {
        return true;
    }
}
