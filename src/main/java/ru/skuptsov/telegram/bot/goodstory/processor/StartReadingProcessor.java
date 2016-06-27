package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialogStore;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.client.command.impl.SendMessageCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.MessageTextEventProcessor;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.of;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@Component
public class StartReadingProcessor implements MessageTextEventProcessor {
    private static final String STARTREADING = "/startreading";
    private static final String START = "/start";

    @Autowired
    private UserDialogStore userDialogStore;

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {
        SendMessage sendMessage = new SendMessage();

        Long chatId = updateEvent.getUpdate().getMessage().getChatId();
        sendMessage.setChatId(updateEvent.getUpdate().getMessage().getChatId().toString());

        UserDialog userDialog = userDialogStore.startUserDialog(chatId);

        DialogState dialogState = DialogState.START.getNext();

        userDialog.setDialogState(dialogState);

        userDialogStore.updateUserDialog(chatId, userDialog);

        sendMessage.setText(dialogState.getDialogText());

        sendMessage.setReplayMarkup(dialogState.getReplyKeyboard());

        return SendMessageCommand.builder()
                .sendMessage(sendMessage)
                .build();
    }

    @Override
    public Set<String> getMessageText() {
        return of(STARTREADING, START);
    }
}
