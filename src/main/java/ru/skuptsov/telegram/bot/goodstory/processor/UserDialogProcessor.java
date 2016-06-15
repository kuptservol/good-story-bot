package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import ru.skuptsov.telegram.bot.goodstory.dialog.DialogEnum;
import ru.skuptsov.telegram.bot.goodstory.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.dialog.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.story.StoryService;
import ru.skuptsov.telegram.bot.platform.client.command.ApiCommand;
import ru.skuptsov.telegram.bot.platform.client.command.impl.EditMessageTextCommand;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.CallbackQueryDataEventProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Kuptsov
 * @since 07/06/2016
 */
@Component
public class UserDialogProcessor implements CallbackQueryDataEventProcessor {

    private final List<String> dialogCallbacks =
            Arrays.stream(DialogState.values())
                    .flatMap(dialogState -> Arrays.stream(dialogState.getDialogEnumClass().getEnumConstants()))
                    .map(DialogEnum::getCallbackData)
                    .collect(Collectors.toList());

    @Override
    public List<String> getCallbackQueryData() {
        return dialogCallbacks;
    }

    @Autowired
    private UserDialogStore userDialogStore;

    @Autowired
    private StoryService storyService;

    @Override
    public ApiCommand process(UpdateEvent updateEvent) {

        EditMessageText sendMessage = new EditMessageText();

        Long chatId = updateEvent.getUpdate().getCallbackQuery().getMessage().getChatId();

        UserDialog userDialog = userDialogStore.getUserDialog(chatId);

        sendMessage.setChatId(chatId.toString());
        sendMessage.setMessageId(updateEvent.getUpdate().getCallbackQuery().getMessage().getMessageId());

        DialogState dialogState = userDialog.getDialogState().getNext();

        if (dialogState != DialogState.FINISH) {
            sendMessage.setText(dialogState.getDialogText());
            sendMessage.setReplyMarkup(dialogState.getReplyKeyboard());
            userDialog.setDialogState(dialogState);

            //update query
            userDialogStore.updateUserDialog(chatId, userDialog);
        } else {
            sendMessage.setText(storyService.getStory(userDialog.getStoryQuery()));
            userDialogStore.finishUserDialog(chatId);
        }

        return EditMessageTextCommand.builder()
                .editMessageText(sendMessage)
                .build();
    }
}
