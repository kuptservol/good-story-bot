package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface DialogEnum {

    String getText();

    String getCallbackData();

    DialogEnum fromCallbackData(String callbackData);
}
