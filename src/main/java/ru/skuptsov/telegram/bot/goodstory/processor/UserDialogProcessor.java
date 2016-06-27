package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import ru.skuptsov.telegram.bot.goodstory.dialog.Dialog;
import ru.skuptsov.telegram.bot.goodstory.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.story.StoryService;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.client.command.impl.EditMessageTextCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.CallbackQueryDataEventProcessor;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static ru.skuptsov.telegram.bot.goodstory.dialog.DialogState.BACK_CALLBACK;

/**
 * @author Sergey Kuptsov
 * @since 07/06/2016
 */
@Component
public class UserDialogProcessor implements CallbackQueryDataEventProcessor {

    private final Set<String> dialogCallbacks =
            stream(DialogState.values())
                    .flatMap(dialogState -> stream(dialogState.getDialog().getEnumConstants()))
                    .map(Dialog::getCallbackData)
                    .collect(toSet());
    @Autowired
    private UserDialogStore userDialogStore;
    @Autowired
    private StoryService storyService;

    @Override
    public Set<String> getCallbackQueryData() {
        return dialogCallbacks;
    }

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {
        Long chatId = updateEvent.getUpdate().getCallbackQuery().getMessage().getChatId();

        EditMessageText sendMessage = createMessage(updateEvent, chatId);

        UserDialog userDialog = userDialogStore.getUserDialog(chatId);

        DialogState currentDialogState = userDialog.getDialogState();
        String callbackData = updateEvent.getUpdate().getCallbackQuery().getData();

        DialogState dialogState;
        if (callbackData.equals(BACK_CALLBACK)) {
            dialogState = currentDialogState.getPrevious();
        } else {
            currentDialogState.getDialog().getEnumConstants()[0]
                    .updateStoryQuery(userDialog.getStoryQuery(), callbackData);

            dialogState = currentDialogState.getNext();
        }

        if (dialogState != DialogState.FINISH) {
            sendMessage.setText(dialogState.getDialogText());
            sendMessage.setReplyMarkup(dialogState.getReplyKeyboard());
            userDialog.setDialogState(dialogState);

            userDialogStore.updateUserDialog(chatId, userDialog);
        } else {
            sendMessage.setText(storyService.getStory(userDialog.getStoryQuery()));
            userDialogStore.finishUserDialog(chatId);
        }

        return EditMessageTextCommand.builder()
                .editMessageText(sendMessage)
                .build();
    }

    private EditMessageText createMessage(UpdateEvent updateEvent, Long chatId) {
        EditMessageText sendMessage = new EditMessageText();

        sendMessage.setChatId(chatId.toString());
        sendMessage.setMessageId(updateEvent.getUpdate().getCallbackQuery().getMessage().getMessageId());
        return sendMessage;
    }
}
