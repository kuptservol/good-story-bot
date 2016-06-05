package ru.skuptsov.telegram.bot.goodstory.client;

import com.fasterxml.jackson.databind.JavaType;
import org.json.JSONObject;
import ru.skuptsov.telegram.bot.goodstory.client.exception.TelegramBotApiException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface TelegramBotHttpClient {
    <T> Future<T> executeGet(@NotNull String method,
                             @Nullable Map<String, String> params,
                             @NotNull JavaType returnType) throws TelegramBotApiException;

    <T, V> Future<T> executePost(
            @NotNull String method,
            @Nullable V requestObject,
            @NotNull JavaType returnType) throws TelegramBotApiException;
}
