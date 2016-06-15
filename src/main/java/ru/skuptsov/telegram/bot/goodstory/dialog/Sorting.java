package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Sorting implements DialogEnum {
    NEWEST("новое", "sorting.new"),
    USER_RATING("по рейтингу", "sorting.rating"),
    RANDOM("случайное", "sorting.random");

    private final String text, callbackData;

    Sorting(String text, String callbackData) {
        this.text = text;
        this.callbackData = callbackData;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getCallbackData() {
        return callbackData;
    }
}
