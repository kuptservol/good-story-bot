package ru.skuptsov.telegram.bot.goodstory.dialog;

import ru.skuptsov.telegram.bot.goodstory.story.StoryQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface Dialog {

    String getText();

    String getCallbackData();

    void updateStoryQuery(@NotNull StoryQuery storyQuery, @NotNull String callbackData);
}
