package ru.skuptsov.telegram.bot.goodstory.client.impl;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.goodstory.client.NextOffsetStrategy;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotHttpClient;

import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static ru.skuptsov.telegram.bot.goodstory.client.utils.JavaTypeUtils.listTypeOf;
import static ru.skuptsov.telegram.bot.goodstory.client.utils.JavaTypeUtils.simpleTypeOf;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public class TelegramBotApiImpl implements TelegramBotApi {
    @Autowired
    private NextOffsetStrategy nextOffsetStrategy;

    private final TelegramBotHttpClient client;

    public TelegramBotApiImpl(TelegramBotHttpClient client) {
        this.client = client;
    }

    @Override
    @Timed(name = "bot.api.client.getNextUpdates", absolute = true)
    public List<Update> getNextUpdates(Integer poolingLimit, Integer poolingTimeout) {
        List<Update> updates = client.executeGet(
                "getUpdates",
                of("offset", nextOffsetStrategy.getNextOffset().toString(),
                        "timeout", poolingTimeout.toString(),
                        "limit", poolingLimit.toString()),
                listTypeOf(Update.class));

        nextOffsetStrategy.saveCurrentOffset(updates);

        return updates;
    }

    @Override
    public Message sendMessage(SendMessage sendMessage, boolean async) {
        return client.executePost(
                sendMessage.getPath(),
                sendMessage,
                simpleTypeOf(Message.class));
    }
}
