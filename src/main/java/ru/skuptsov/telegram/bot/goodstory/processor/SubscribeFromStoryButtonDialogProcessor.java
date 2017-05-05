package ru.skuptsov.telegram.bot.goodstory.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.client.command.ReplyTo;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import java.util.ArrayList;
import java.util.List;

import static ru.skuptsov.telegram.bot.goodstory.processor.DialogUtils.getChatIdFromCallback;

@MessageHandler
public class SubscribeFromStoryButtonDialogProcessor {
    public static final String SUBSCRIBE_BUTTON_CALLBACK = "subscribe.story.button.callback";

    @Autowired
    private UserDialogStore userDialogStore;

    @MessageMapping(callback = SUBSCRIBE_BUTTON_CALLBACK)
    public Reply showSubscribeDialog(UpdateEvent updateEvent) {
        Long chatId = getChatIdFromCallback(updateEvent);
        StoryUserDialog userDialog = userDialogStore.getUserDialog(chatId);
        userDialog.setAwaitingSubscriptionTime(true);
        userDialogStore.updateUserDialog(chatId, userDialog);

        return ReplyTo.to(chatId.toString()).withMessage(
                "\nНовый рассказ будет приходить каждый день в указанное время.\n\n" +
                        "Укажите желаемый час дня (например: 13 или 7):"
        );
    }


    public InlineKeyboardMarkup getReplyKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> internalKeyboard = new ArrayList<>();
        keyboard.add(internalKeyboard);

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Подписаться");
        button.setCallbackData(SUBSCRIBE_BUTTON_CALLBACK);
        internalKeyboard.add(button);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}
