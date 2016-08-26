package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Dialog;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;
import ru.skuptsov.telegram.bot.platform.client.command.MessageResponse;
import ru.skuptsov.telegram.bot.platform.handler.CallbackQueryDataMessageHandler;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.BACK_CALLBACK;

/**
 * @author Sergey Kuptsov
 * @since 07/06/2016
 */
@Component
public class UserDialogProcessor implements CallbackQueryDataMessageHandler {

    private final Set<String> dialogCallbacks =
            stream(DialogState.values())
                    .flatMap(dialogState -> stream(dialogState.getDialog().getEnumConstants()))
                    .map(Dialog::getCallbackData)
                    .collect(Collectors.toSet());


    @Autowired
    private UserDialogStore userDialogStore;

    @Autowired
    private StoryService storyService;

    @Override
    public Set<String> getCallbackQueryData() {
        return dialogCallbacks;
    }

    public MessageResponse handle(UpdateEvent updateEvent) {
        Long chatId = updateEvent.getUpdate().getCallbackQuery().getMessage().getChatId();

        EditMessageText editMessageText = createMessage(updateEvent, chatId);

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
            editMessageText.setText(dialogState.getDialogText());
            editMessageText.setReplyMarkup(dialogState.getReplyKeyboard());
            userDialog.setDialogState(dialogState);

            userDialogStore.updateUserDialog(chatId, userDialog);
        } else {
            editMessageText.setText(storyService.getStory(
                    userDialog.getStoryQuery(),
                    updateEvent.getUpdate().getCallbackQuery().getMessage().getFrom().getId())
                    .map(Story::getText)
                    .orElse("Новых текство не найдено"));
            userDialogStore.finishUserDialog(chatId);
        }

        return MessageResponse.editMessageText(editMessageText);
    }

    private EditMessageText createMessage(UpdateEvent updateEvent, Long chatId) {
        EditMessageText sendMessage = new EditMessageText();

        sendMessage.setChatId(chatId.toString());
        sendMessage.setMessageId(updateEvent.getUpdate().getCallbackQuery().getMessage().getMessageId());
        return sendMessage;
    }

}
