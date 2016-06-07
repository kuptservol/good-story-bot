package ru.skuptsov.telegram.bot.platform.worker;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.resolver.EventProcessorResolver;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Component
public class WorkerTaskFactory {
    private final Logger log = LoggerFactory.getLogger(WorkerTaskFactory.class);

    @Autowired
    private EventProcessorResolver eventProcessorResolver;

    public WorkerTask create(UpdateEvent updateEvent) {
        return new WorkerTask(updateEvent, eventProcessorResolver);
    }

    public static final class WorkerTask {
        private final Logger log = LoggerFactory.getLogger(WorkerTask.class);

        private final UpdateEvent updateEvent;
        private final EventProcessorResolver eventProcessorResolver;

        private WorkerTask(UpdateEvent updateEvent, EventProcessorResolver eventProcessorResolver) {
            this.updateEvent = updateEvent;
            this.eventProcessorResolver = eventProcessorResolver;
        }

        public ApiCommand execute() {
            log.debug("Processing event {}", updateEvent);

            if (updateEvent == UpdateEvent.EMPTY) {
                log.warn("Event is empty");
                return ApiCommand.EMPTY;
            }

            return processEvent(updateEvent);
        }

        @Timed(name = "worker.task.process.event", absolute = true)
        private ApiCommand processEvent(UpdateEvent updateEvent) {
            try {
                EventProcessor eventProcessor = eventProcessorResolver.resolve(updateEvent);
                return eventProcessor.process(updateEvent);
            } catch (Exception ex) {
                log.error("Error ocurred while processing event {}", updateEvent, ex);
            }

            return ApiCommand.EMPTY;

        }
    }
}
