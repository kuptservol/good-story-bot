package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@ToString
@Builder
@Getter
@Setter
public class StoryUserDialog {
    private DialogState dialogState;
    private StoryQuery storyQuery;
    private Type type;
    private boolean awaitingSubscriptionTime;

    public enum Type {
        READ,
        SUBSCRIBE
    }
}
