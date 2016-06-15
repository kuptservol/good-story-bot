package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum EmptyDialog implements DialogEnum {
;
    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getCallbackData() {
        return "";
    }
}
