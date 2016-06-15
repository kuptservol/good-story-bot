package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Genre implements DialogEnum{
    DETECTIVE("детектив", "genre.detective"),
    FANTASTIC("фантастика", "genre.fantastica"),;

    private final String text, callbackData;

    Genre(String text, String callbackData) {
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
