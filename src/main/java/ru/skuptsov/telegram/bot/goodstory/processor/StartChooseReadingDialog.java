package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.client.command.impl.SendMessageCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.CommandEventProcessor;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@Component
public class StartChooseReadingDialog implements CommandEventProcessor {
    private static final String STARTREADING = "/startreading";
    private static final String START = "/start";

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(updateEvent.getUpdate().getMessage().getChatId().toString());
        sendMessage.setText("Выберите длительность рассказа");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButtonLength1 = new InlineKeyboardButton();
        inlineKeyboardButtonLength1.setText("5 мин");
        inlineKeyboardButtonLength1.setCallbackData("5 мин");

        InlineKeyboardButton inlineKeyboardButtonLength2 = new InlineKeyboardButton();
        inlineKeyboardButtonLength2.setText("10 мин");
        inlineKeyboardButtonLength2.setCallbackData("10 мин");

        InlineKeyboardButton inlineKeyboardButtonLength3 = new InlineKeyboardButton();
        inlineKeyboardButtonLength3.setText("15 мин");
        inlineKeyboardButtonLength3.setCallbackData("15 мин");

        List<List<InlineKeyboardButton>> keyboard = of(of(inlineKeyboardButtonLength1,
                inlineKeyboardButtonLength2,
                inlineKeyboardButtonLength3));

        inlineKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplayMarkup(inlineKeyboardMarkup);

        return SendMessageCommand.builder()
                .sendMessage(sendMessage)
                .build();
    }

    @Override
    public List<String> getCommandText() {
        return of(STARTREADING, START);
    }
}
