package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

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
    TYPE(LENGTH, Type.class, "Выберите тип"),
    START(TYPE, EmptyDialog.class, "");

    public static final String BACK_CALLBACK = "back";
    private static final Map<DialogState, DialogState> previousValue =
            stream(DialogState.values())
                    .collect(toMap(DialogState::getNext, identity()));
    private final DialogState next;
    private final Class<? extends Dialog> dialog;
    private final String dialogText;
    private final InlineKeyboardMarkup replyKeyboard;

    DialogState(DialogState next, Class<? extends Dialog> dialog, String dialogText) {
        this.next = next;
        this.dialog = dialog;
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

    public Class<? extends Dialog> getDialog() {
        return dialog;
    }

    public InlineKeyboardMarkup getInlineKeyBoardMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> internalKeyboard = new ArrayList<>();
        keyboard.add(internalKeyboard);

        for (Dialog dialog : this.dialog.getEnumConstants()) {
            InlineKeyboardButton inlineKeyboardButtonLength = new InlineKeyboardButton();
            inlineKeyboardButtonLength.setText(dialog.getText());
            inlineKeyboardButtonLength.setCallbackData(dialog.getCallbackData());
            internalKeyboard.add(inlineKeyboardButtonLength);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    public DialogState getPrevious() {
        return previousValue.get(this);
    }
}
