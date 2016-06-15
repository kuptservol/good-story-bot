package ru.skuptsov.telegram.bot.goodstory.dialog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skuptsov.telegram.bot.goodstory.story.StoryQuery;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@ToString
@Builder
@Getter
@Setter
public class UserDialog {

    private DialogState dialogState;
    private StoryQuery storyQuery;
}
