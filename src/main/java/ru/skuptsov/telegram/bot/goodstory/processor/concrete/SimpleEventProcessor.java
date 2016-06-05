package ru.skuptsov.telegram.bot.goodstory.processor.concrete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.goodstory.client.command.impl.SendMessageCommand;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;
import ru.skuptsov.telegram.bot.goodstory.processor.EventProcessor;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Component
public class SimpleEventProcessor implements EventProcessor {
    private final Logger log = LoggerFactory.getLogger(SimpleEventProcessor.class);

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {
        log.debug("Processing event {}", updateEvent);
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(updateEvent.getUpdate().getMessage().getChatId().toString());
        sendMessage.setText("Пашель нахуй!!!");

        return SendMessageCommand.builder()
                .callback((message) -> log.debug("!!!!!!!"))
                .sendMessage(sendMessage)
                .build();
    }

    @Override
    public boolean isSuitableForProcessingEvent(UpdateEvent updateEvent) {
        return true;
    }
}
