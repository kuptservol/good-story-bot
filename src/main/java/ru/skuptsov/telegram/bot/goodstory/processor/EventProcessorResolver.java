package ru.skuptsov.telegram.bot.goodstory.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;

import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Component
//todo: event type?
public class EventProcessorResolver {
    private final Logger log = LoggerFactory.getLogger(EventProcessorResolver.class);

    @Autowired
    private List<EventProcessor> eventProcessors;

    public EventProcessor resolve(UpdateEvent updateEvent) {
        log.debug("Resolving event {}", updateEvent);
        return eventProcessors.stream()
                .filter(eventProcessor -> eventProcessor.isSuitableForProcessingEvent(updateEvent))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable process resolver found for event +" + updateEvent));
    }
}
