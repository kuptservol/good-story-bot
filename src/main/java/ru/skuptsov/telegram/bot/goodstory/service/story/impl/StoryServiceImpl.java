package ru.skuptsov.telegram.bot.goodstory.service.story.impl;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@Service
public class StoryServiceImpl implements StoryService {
    private final static Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    @Autowired
    private StoryRepository storyRepository;

    @Override
    @Timed(name = "story.service.get.story", absolute = true)
    public Optional<Story> getStory(@NotNull StoryQuery storyQuery, @NotNull int userId) {
        log.debug("Received request for story by query [{}] with user [{}]", storyQuery, userId);

        Optional<Story> story = getStory_(storyQuery, userId);

        log.debug("Received story [{}] by query [{}] with user [{}]", story, storyQuery, userId);

        return story;
    }

    private Optional<Story> getStory_(@NotNull StoryQuery storyQuery, @NotNull int userId) {
        Optional<Story> story;
        if (storyQuery.getType() == Type.STORY) {
            story = ofNullable(storyRepository.getStoryUnseen(storyQuery, userId));
            story.ifPresent(story_ -> storyRepository.markStoryAsSeen(story_.getId(), userId));
        } else if (storyQuery.getType() == Type.PART) {
            story = ofNullable(storyRepository.getStoryUnseen(storyQuery, userId));
        } else {
            story = Optional.empty();
            log.error("Story type {} not implemented", storyQuery.getType());
        }
        return story;
    }
}
