package ru.skuptsov.telegram.bot.goodstory.client.command.impl;

import lombok.Builder;
import lombok.Getter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;

/**
 * @author Sergey Kuptsov
 * @since 02/06/2016
 */
@Builder
@Getter
public class SendMessageCommand extends AbstractApiCommand<Message> {

    private SendMessage sendMessage;

    @Override
    public Message execute(TelegramBotApi telegramBotApi) {
        return telegramBotApi;
    }
}
