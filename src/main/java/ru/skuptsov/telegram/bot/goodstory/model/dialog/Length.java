package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.closedOpen;
import static com.google.common.collect.Range.open;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState.BACK_CALLBACK;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.Length.Constants.READ_IN_MINUTE;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Length implements Dialog {
    ONE_MINUTE("одна минута", "length.one_minute", 1, closedOpen(30, READ_IN_MINUTE)),
    FIVE_MINUTE("пять минут", "length.five_minute", 1, open(READ_IN_MINUTE, READ_IN_MINUTE * 2)),
    TEN_MINUTE("десять минут", "length.ten_minute", 1, closed(READ_IN_MINUTE * 2, READ_IN_MINUTE * 3)),
    BACK("назад", BACK_CALLBACK, 2, closed(-1, -1));

    private static final Map<String, Length> valuesMap =
            stream(Length.values())
                    .collect(toMap(Length::getCallbackData, identity()));
    private final String text, callbackData;
    private final int keyboardLine;
    private final Range<Integer> wordsNumberRange;

    Length(
            String text,
            String callbackData,
            int keyboardLine,
            Range<Integer> wordsNumberRange) {
        this.text = text;
        this.callbackData = callbackData;
        this.keyboardLine = keyboardLine;
        this.wordsNumberRange = wordsNumberRange;
    }

    @Override
    public int getKeyboardLine() {
        return keyboardLine;
    }

    public Range<Integer> getWordsNumberRange() {
        return wordsNumberRange;
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

    public Length getLengthByTextLength(int length) {
        return lengthRange.get(length);
    }

    private static RangeMap<Integer, Length> lengthRange;

    static {
        ImmutableRangeMap.Builder<Integer, Length> builder = ImmutableRangeMap.builder();
        Arrays.stream(Length.values()).forEach(
                value -> builder.put(value.wordsNumberRange, value));
        lengthRange = builder.build();
    }

    public static class Constants {
        public static final int READ_IN_MINUTE = 180;
    }
}
