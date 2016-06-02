package ru.skuptsov.telegram.bot.goodstory.client.command.impl;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommandSender;
import ru.skuptsov.telegram.bot.goodstory.config.ApiCommandSenderConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

import static com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination;
import static ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommand.EMPTY_CALLBACK;

/**
 * @author Sergey Kuptsov
 * @since 01/06/2016
 */
@Component
public class ApiCommandSenderImpl extends AbstractExecutionThreadService implements ApiCommandSender {
    private Logger log = LoggerFactory.getLogger(ApiCommandSenderImpl.class);

    @Autowired
    private ApiCommandSenderConfiguration apiCommandSenderConfiguration;

    @Autowired
    @Qualifier("commandSenderBotApi")
    private TelegramBotApi telegramBotApi;

    private BlockingQueue<ApiCommand> apiCommandQueue;

    private ExecutorService apiCommandSenderExecutor;

    @Override
    public void sendCommand(ApiCommand apiCommand) {
        log.trace("Sending api command {}", apiCommand);
        try {
            apiCommandQueue.offer(apiCommand, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Can't send command {}", apiCommand);
        }
    }

    @Override
    //todo: try to generify
    protected void run() throws Exception {
        while (isRunning()) {
            ApiCommand apiCommand = apiCommandQueue.take();
            CompletableFuture<?> sendCommandFuture =
                    CompletableFuture.supplyAsync(() -> apiCommand.execute(telegramBotApi), apiCommandSenderExecutor);

            if (apiCommand.getCallback() != EMPTY_CALLBACK) {
                sendCommandFuture.thenAcceptAsync(result -> apiCommand.getCallback().accept(result));
            }
        }
    }

    @Override
    protected void startUp() {
        apiCommandQueue = new ArrayBlockingQueue<>(apiCommandSenderConfiguration.getQueueSize());

        apiCommandSenderExecutor = Executors.newFixedThreadPool(
                apiCommandSenderConfiguration.getThreadCount(),
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("UpdatesWorkerTask-%d")
                        .build());
    }

    @Override
    protected void triggerShutdown() {
        shutdownAndAwaitTermination(
                apiCommandSenderExecutor,
                2,
                TimeUnit.SECONDS);
    }

    @PostConstruct
    public void init() {
        startAsync()
                .awaitRunning();
    }

    @PreDestroy
    public void destroy() {
        stopAsync()
                .awaitTerminated();
    }
}
