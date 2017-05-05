package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Dialog;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;
import ru.skuptsov.telegram.bot.goodstory.processor.story.StoryTextBuilder;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.SubscribeService;
import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.handler.CallbackQueryDataMessageHandler;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.model.updatingmessages.EditMessageText;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.BACK_CALLBACK;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.START;
import static ru.skuptsov.telegram.bot.goodstory.processor.DialogUtils.getChatIdFromCallback;

/**
 * @author Sergey Kuptsov
 * @since 07/06/2016
 */
@Component
public class UserStoryDialogProcessor implements CallbackQueryDataMessageHandler {

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
    private SubscribeFromStoryButtonDialogProcessor subscribeFromStoryButtonDialogProcessor;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private StoryService storyService;

    @Override
    public Set<String> getCallbackQueryData() {
        return dialogCallbacks;
    }

    public Reply handle(UpdateEvent updateEvent) {
        Long chatId = updateEvent.getUpdate().getCallbackQuery().getMessage().getChatId();

        EditMessageText editMessageText = createMessage(updateEvent, chatId);

        StoryUserDialog storyUserDialog = userDialogStore.getUserDialog(chatId);

        DialogState currentDialogState = storyUserDialog.getDialogState();
        String callbackData = updateEvent.getUpdate().getCallbackQuery().getData();

        DialogState dialogState;
        if (callbackData.equals(BACK_CALLBACK) && currentDialogState != START) {
            dialogState = currentDialogState.getPrevious();
        } else {
            if (currentDialogState != START) {
                currentDialogState.getDialog().getEnumConstants()[0]
                        .updateStoryQuery(storyUserDialog.getStoryQuery(), callbackData);
            }

            dialogState = currentDialogState.getNext();
        }

        if (dialogState != DialogState.FINISH) {
            editMessageText.setText(dialogState.getDialogText());
            editMessageText.setReplyMarkup(dialogState.getReplyKeyboard());
            storyUserDialog.setDialogState(dialogState);

            userDialogStore.updateUserDialog(chatId, storyUserDialog);
        } else {
            if (storyUserDialog.getType() == StoryUserDialog.Type.READ) {
                editMessageText.setText(storyTextBuilder.build(getStory(updateEvent, storyUserDialog)));
                if (showSubscribeButton(chatId)) {
                    addSubscribeButton(editMessageText);
                } else {
                    userDialogStore.finishUserDialog(chatId);
                }
            } else if (storyUserDialog.getType() == StoryUserDialog.Type.SUBSCRIBE) {
                return subscribeFromStoryButtonDialogProcessor.showSubscribeDialog(updateEvent);
            }
        }

        return Reply.withEditMessageText(editMessageText);
    }

    private void addSubscribeButton(EditMessageText editMessageText) {
        editMessageText.setReplyMarkup(subscribeFromStoryButtonDialogProcessor.getReplyKeyboard());
    }

    private boolean showSubscribeButton(Long chatId) {
        return !subscribeService.get(chatId).isPresent();
    }

    private Optional<Story> getStory(UpdateEvent updateEvent, StoryUserDialog storyUserDialog) {
        return storyService.getStory(
                storyUserDialog.getStoryQuery(),
                getChatIdFromCallback(updateEvent));
    }

    private EditMessageText createMessage(UpdateEvent updateEvent, Long chatId) {
        EditMessageText sendMessage = new EditMessageText();

        sendMessage.setChatId(chatId.toString());
        sendMessage.setMessageId(updateEvent.getUpdate().getCallbackQuery().getMessage().getMessageId());
        return sendMessage;
    }

}
