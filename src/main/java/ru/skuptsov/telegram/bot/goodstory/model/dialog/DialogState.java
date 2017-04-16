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
    //    RATING(FINISH, Sorting.class, "Выберите сортировку"),
//    LANGUAGE(FINISH, Language.class, "Язык"),
    GENRE(FINISH, Genre.class, "Жанр"),
    LENGTH(GENRE, Length.class, "Длительность"),
    TYPE(LENGTH, Type.class, "Тип"),
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
        int currKeyBoardLineIndex = 1;
        keyboard.add(internalKeyboard);

        for (Dialog dialog : this.dialog.getEnumConstants()) {
            if (dialog.getKeyboardLine() != currKeyBoardLineIndex) {
                internalKeyboard = new ArrayList<>();
                currKeyBoardLineIndex++;
                keyboard.add(internalKeyboard);
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(dialog.getText());
            button.setCallbackData(dialog.getCallbackData());
            internalKeyboard.add(button);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    public DialogState getPrevious() {
        return previousValue.get(this);
    }
}
