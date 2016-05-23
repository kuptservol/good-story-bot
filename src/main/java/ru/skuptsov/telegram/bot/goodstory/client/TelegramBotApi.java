package ru.skuptsov.telegram.bot.goodstory.client;

import org.telegram.telegrambots.api.objects.Update;

import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public interface TelegramBotApi {

    List<Update> getNextUpdates(Integer poolingLimit, Integer poolingTimeout);
}
