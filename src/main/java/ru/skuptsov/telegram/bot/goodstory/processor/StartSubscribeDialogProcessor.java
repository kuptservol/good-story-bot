package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import static ru.skuptsov.telegram.bot.goodstory.processor.DialogUtils.getChatId;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@MessageHandler
public class StartSubscribeDialogProcessor {
    private static final String SUBSCRIBE_RUS = "/подписаться";
    private static final String SUBSCRIBE = "/subscribe";

    @Autowired
    private UserDialogStore userDialogStore;

    @MessageMapping(text = {SUBSCRIBE_RUS, SUBSCRIBE})
    public Reply process(UpdateEvent updateEvent) {
        SendMessage sendMessage = new SendMessage();

        Long chatId = getChatId(updateEvent);

        sendMessage.setChatId(chatId.toString());

        StoryUserDialog storyUserDialog = userDialogStore.startUserDialog(chatId, StoryUserDialog.Type.SUBSCRIBE);

        DialogState dialogState = DialogState.START.getNext();

        storyUserDialog.setDialogState(dialogState);

        userDialogStore.updateUserDialog(chatId, storyUserDialog);

        sendMessage.setText(dialogState.getDialogText());
        sendMessage.setReplyMarkup(dialogState.getReplyKeyboard());

        return Reply.withMessage(sendMessage);
    }
}
