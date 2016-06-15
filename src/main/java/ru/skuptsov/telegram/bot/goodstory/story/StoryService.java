package ru.skuptsov.telegram.bot.goodstory.story;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface StoryService {

    String getStory(StoryQuery storyQuery);
}
