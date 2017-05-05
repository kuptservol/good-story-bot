package ru.skuptsov.telegram.bot.goodstory.processor;

import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.client.command.ReplyTo;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@MessageHandler
public class AboutDialogProcessor {
    private static final String ABOUT = "/about";

    @MessageMapping(text = {ABOUT})
    public Reply process(UpdateEvent updateEvent) {
        return ReplyTo.to(updateEvent)
                .withMessage("Based on https://github.com/kuptservol/spring-telegram-bot-starter");
    }
}
