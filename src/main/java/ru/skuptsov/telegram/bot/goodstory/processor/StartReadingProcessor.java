package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.platform.client.command.MessageResponse;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import static ru.skuptsov.telegram.bot.platform.client.command.MessageResponse.sendMessage;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@MessageHandler
public class StartReadingProcessor {
    private static final String STARTREADING = "/read";
    private static final String STARTREADING_OLD = "/startreading";
    private static final String START = "/start";

    @Autowired
    private UserDialogStore userDialogStore;

    @MessageMapping(text = {STARTREADING, START, STARTREADING_OLD})
    public MessageResponse process(UpdateEvent updateEvent) {
        SendMessage sendMessage = new SendMessage();

        Long chatId = updateEvent.getUpdate().getMessage().getChatId();

        sendMessage.setChatId(chatId.toString());

        UserDialog userDialog = userDialogStore.startUserDialog(chatId);

        DialogState dialogState = DialogState.START.getNext();

        userDialog.setDialogState(dialogState);

        userDialogStore.updateUserDialog(chatId, userDialog);

        sendMessage.setText(dialogState.getDialogText());
        sendMessage.setReplyMarkup(dialogState.getReplyKeyboard());

        return sendMessage(sendMessage);
    }
}
