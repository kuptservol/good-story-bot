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
public enum Length implements Dialog {
    ONE_MINUTE("одна минута", "length.one_minute", 1),
    FIVE_MINUTE("пять минут", "length.five_minute", 1),
    TEN_MINUTE("десять минут", "length.ten_minute", 1),
    BACK("назад", BACK_CALLBACK, 2);

    private static final Map<String, Length> valuesMap =
            stream(Length.values())
                    .collect(toMap(Length::getCallbackData, identity()));
    private final String text, callbackData;
    private final int keyboardLine;

    Length(String text, String callbackData, int keyboardLine) {
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
                .ifPresent(storyQuery::setLength);
    }
}
