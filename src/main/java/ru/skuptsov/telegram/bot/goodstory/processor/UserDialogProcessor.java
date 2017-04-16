package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Dialog;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.processor.story.StoryTextBuilder;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;
import ru.skuptsov.telegram.bot.platform.client.command.MessageResponse;
import ru.skuptsov.telegram.bot.platform.handler.CallbackQueryDataMessageHandler;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.model.updatingmessages.EditMessageText;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.BACK_CALLBACK;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.START;

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
    private StoryTextBuilder storyTextBuilder;

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
        if (callbackData.equals(BACK_CALLBACK) && currentDialogState != START) {
            dialogState = currentDialogState.getPrevious();
        } else {
            if (currentDialogState != START) {
                currentDialogState.getDialog().getEnumConstants()[0]
                        .updateStoryQuery(userDialog.getStoryQuery(), callbackData);
            }

            dialogState = currentDialogState.getNext();
        }

        if (dialogState != DialogState.FINISH) {
            editMessageText.setText(dialogState.getDialogText());
            editMessageText.setReplyMarkup(dialogState.getReplyKeyboard());
            userDialog.setDialogState(dialogState);

            userDialogStore.updateUserDialog(chatId, userDialog);
        } else {
            editMessageText.setText(storyTextBuilder.build(getStory(updateEvent, userDialog)));
            userDialogStore.finishUserDialog(chatId);
        }

        return MessageResponse.editMessageText(editMessageText);
    }

    private Optional<Story> getStory(UpdateEvent updateEvent, UserDialog userDialog) {
        return storyService.getStory(
                userDialog.getStoryQuery(),
                getUserId(updateEvent));
    }

    private Integer getUserId(UpdateEvent updateEvent) {
        return updateEvent.getUpdate().getCallbackQuery().getMessage().getFrom().getId();
    }

    private EditMessageText createMessage(UpdateEvent updateEvent, Long chatId) {
        EditMessageText sendMessage = new EditMessageText();

        sendMessage.setChatId(chatId.toString());
        sendMessage.setMessageId(updateEvent.getUpdate().getCallbackQuery().getMessage().getMessageId());
        return sendMessage;
    }

}
