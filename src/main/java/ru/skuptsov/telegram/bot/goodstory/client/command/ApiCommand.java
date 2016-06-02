package ru.skuptsov.telegram.bot.goodstory.client.command;

import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;

import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
public interface ApiCommand<T> {
    ApiCommand EMPTY = telegramBotApi -> new Object();

    Consumer EMPTY_CALLBACK = o -> {
    };

    T execute(TelegramBotApi telegramBotApi);

    default Consumer<T> getCallback() {
        return EMPTY_CALLBACK;
    }
}
