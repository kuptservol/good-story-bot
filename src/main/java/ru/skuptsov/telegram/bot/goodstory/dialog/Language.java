package ru.skuptsov.telegram.bot.goodstory.dialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Language implements DialogEnum{
    RUSSIAN("русский", "languauge.russian"),
    ENGLISH("английский", "languauge.english"),;

    private final String text, callbackData;

    Language(String text, String callbackData) {
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
