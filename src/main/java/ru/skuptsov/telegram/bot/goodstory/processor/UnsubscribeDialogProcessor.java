package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.SubscribeService;
import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import static ru.skuptsov.telegram.bot.platform.client.command.Reply.withMessage;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@MessageHandler
public class UnsubscribeDialogProcessor {
    public static final String UNSUBSCRIBE = "/unsubscribe";
    private static final String UNSUBSCRIBE_RUS = "/отписаться";

    @Autowired
    private SubscribeService subscribeService;

    @MessageMapping(text = {UNSUBSCRIBE, UNSUBSCRIBE_RUS})
    public Reply process(UpdateEvent updateEvent) {
        SendMessage sendMessage = new SendMessage();

        Long chatId = updateEvent.getUpdate().getMessage().getChatId();

        sendMessage.setChatId(chatId);

        subscribeService.remove(chatId);

        sendMessage.setText("Вы успешно отписались!");

        return withMessage(sendMessage);
    }
}
