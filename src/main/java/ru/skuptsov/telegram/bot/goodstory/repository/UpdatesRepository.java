package ru.skuptsov.telegram.bot.goodstory.repository;

import com.codahale.metrics.annotation.Counted;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.config.UpdatesRepositoryConfiguration;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvents;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Repository
public class UpdatesRepository {
    private final static Logger logger = LoggerFactory.getLogger(UpdatesRepository.class);

    @Autowired
    private UpdatesRepositoryConfiguration updatesRepositoryConfiguration;

    private final ListeningExecutorService repositoryExecutor = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadExecutor(
                    new ThreadFactoryBuilder()
                            .setDaemon(true)
                            .setNameFormat("UpdatesRepositoryThread-%d")
                            .build()
            )
    );

    @Autowired
    private TelegramBotApi botApi;

    @Autowired
    private EventBus eventBus;

    private void poolUpdates() {
        repositoryExecutor.execute(() -> {
            while (true) {
                logger.debug("Start pooling new updates");
                List<Update> updates = botApi.getNextUpdates(
                        updatesRepositoryConfiguration.getPoolingLimit(),
                        updatesRepositoryConfiguration.getPoolingTimeout());

                logger.debug("Received following updates: {}", updates);

                eventBus.post(createUpdateEvents(updates));
            }
        });
    }

    @PostConstruct
    public void init() {
        poolUpdates();
    }

    @PreDestroy
    public void destroy() {
        repositoryExecutor.shutdownNow();
    }

    private UpdateEvents createUpdateEvents(List<Update> updates) {
        return UpdateEvents.builder()
                .updateEventList(updates.stream().map(update -> UpdateEvent.builder()
                        .update(update)
                        .received(now(UTC))
                        .build())
                        .collect(toList()))
                .build();
    }
}
