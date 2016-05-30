package ru.skuptsov.telegram.bot.goodstory.worker;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvents;
import ru.skuptsov.telegram.bot.goodstory.worker.saver.UpdatesWorkerRepository;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Component
public class UpdatesWorker {
    private final static Logger logger = LoggerFactory.getLogger(UpdatesWorker.class);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private UpdatesWorkerRepository updatesWorkerRepository;

    @AllowConcurrentEvents
    @Subscribe
    public void handleUpdateEvents(@NotNull UpdateEvents updateEvents) {
        logger.debug("Received event {}", updateEvents);
        updatesWorkerRepository.save(updateEvents);
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }
}
