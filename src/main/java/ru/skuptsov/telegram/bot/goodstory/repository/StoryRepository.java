package ru.skuptsov.telegram.bot.goodstory.repository;

import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
public interface StoryRepository {

    Story getStoryUnseen(@NotNull StoryQuery storyQuery, int userId);

    void markStoryAsSeen(long storyId, int userId);

    void add(Story story);
}
