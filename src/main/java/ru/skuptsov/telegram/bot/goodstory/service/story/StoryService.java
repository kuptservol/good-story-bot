package ru.skuptsov.telegram.bot.goodstory.service.story;

import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface StoryService {

    Optional<Story> getStory(@NotNull StoryQuery storyQuery, @NotNull int userId);
}
