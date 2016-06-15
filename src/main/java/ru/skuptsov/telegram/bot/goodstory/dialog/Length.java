package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Length implements DialogEnum{
    ONE_MINUTE("одна минута", "length.one_minute"),
    FIVE_MINUTE("пять минут", "length.five_minute"),
    TEN_MINUTE("десять минут", "length.ten_minute");

    private final String text, callbackData;

    Length(String text, String callbackData) {
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
