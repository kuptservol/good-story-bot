package ru.skuptsov.telegram.bot.goodstory.dialog;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@Builder
@Getter
@ToString
public class DialogKeyboard {

    List<InlineKeyboardButton> keyboardButtons;
    private String messageText;
}
