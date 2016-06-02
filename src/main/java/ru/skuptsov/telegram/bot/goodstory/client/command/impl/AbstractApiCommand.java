package ru.skuptsov.telegram.bot.goodstory.client.command.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.api.objects.Message;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommand;

import java.util.function.Consumer;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Getter
@Setter
@ToString
public abstract class AbstractApiCommand<T> implements ApiCommand<T> {

    private Consumer<T> callback = EMPTY_CALLBACK;

}
