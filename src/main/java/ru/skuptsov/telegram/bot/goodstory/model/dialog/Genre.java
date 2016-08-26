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
public enum Genre implements Dialog {
    DETECTIVE("детектив", "genre.detective"),
    FANTASTIC("фантастика", "genre.fantastica"),
    BACK("назад", BACK_CALLBACK);

    private static final Map<String, Genre> valuesMap =
            stream(Genre.values())
                    .collect(toMap(Genre::getCallbackData, identity()));
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

    @Override
    public void updateStoryQuery(@NotNull StoryQuery storyQuery, @NotNull String callbackData) {
        ofNullable(valuesMap.get(callbackData))
                .ifPresent(storyQuery::setGenre);
    }
}
