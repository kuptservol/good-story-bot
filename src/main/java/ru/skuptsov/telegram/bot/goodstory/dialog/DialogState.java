package ru.skuptsov.telegram.bot.goodstory.dialog;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum DialogState {

    FINISH(null, EmptyDialog.class, ""),
    RATING(FINISH, Sorting.class, "Выберите сортировку"),
    LANGUAGE(RATING, Language.class, "Выберите язык"),
    GENRE(LANGUAGE, Genre.class, "Выберите жанр"),
    LENGTH(GENRE, Length.class, "Выберите длительность"),
    START(LENGTH, EmptyDialog.class, "");

    private final DialogState next;
    private final Class<? extends DialogEnum> dialogEnumClass;
    private final String dialogText;
    private final InlineKeyboardMarkup replyKeyboard;

    DialogState(DialogState next, Class<? extends DialogEnum> dialogEnumClass, String dialogText) {
        this.next = next;
        this.dialogEnumClass = dialogEnumClass;
        this.dialogText = dialogText;
        replyKeyboard = getInlineKeyBoardMarkup();
    }

    public DialogState getNext() {
        return next;
    }

    public String getDialogText() {
        return dialogText;
    }

    public InlineKeyboardMarkup getReplyKeyboard() {
        return replyKeyboard;
    }

    public Class<? extends DialogEnum> getDialogEnumClass() {
        return dialogEnumClass;
    }

    public InlineKeyboardMarkup getInlineKeyBoardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> internalKeyboard = new ArrayList<>();
        keyboard.add(internalKeyboard);

        for (DialogEnum dialogEnum : dialogEnumClass.getEnumConstants()) {
            InlineKeyboardButton inlineKeyboardButtonLength = new InlineKeyboardButton();
            inlineKeyboardButtonLength.setText(dialogEnum.getText());
            inlineKeyboardButtonLength.setCallbackData(dialogEnum.getCallbackData());
            internalKeyboard.add(inlineKeyboardButtonLength);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
}
