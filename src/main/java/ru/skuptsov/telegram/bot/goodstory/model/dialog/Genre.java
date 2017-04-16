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
    DETECTIVE("детектив", "genre.detective", 1),
    FANTASTIC("фантастика", "genre.fantastica", 1),
    DRAMA("драма", "genre.drama", 1),
    COMEDY("комедия", "genre.comedy", 2),
    ANY("любой", "genre.any", 2),
    BACK("назад", BACK_CALLBACK, 3);

    private static final Map<String, Genre> valuesMap =
            stream(Genre.values())
                    .collect(toMap(Genre::getCallbackData, identity()));
    private final String text, callbackData;
    private final int keyboardLine;

    Genre(String text, String callbackData, int keyboardLine) {
        this.text = text;
        this.callbackData = callbackData;
        this.keyboardLine = keyboardLine;
    }

    @Override
    public int getKeyboardLine() {
        return keyboardLine;
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
