package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.BACK_CALLBACK;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Language implements Dialog {
    RUSSIAN("русский", "languauge.russian"),
    ENGLISH("английский", "languauge.english"),
    BACK("назад", BACK_CALLBACK);

    private static final Map<String, Language> valuesMap =
            stream(Language.values())
                    .collect(toMap(Language::getCallbackData, identity()));
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

    @Override
    public void updateStoryQuery(@NotNull StoryQuery storyQuery, @NotNull String callbackData) {
        ofNullable(valuesMap.get(callbackData))
                .ifPresent(storyQuery::setLanguage);
    }
}
