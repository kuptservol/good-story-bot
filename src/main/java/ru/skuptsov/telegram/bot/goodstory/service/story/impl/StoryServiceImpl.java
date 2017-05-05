package ru.skuptsov.telegram.bot.goodstory.service.story.impl;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.lang.Character.isWhitespace;
import static java.util.Optional.ofNullable;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.Type.PART;

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
    public Optional<Story> getStory(@NotNull StoryQuery storyQuery, @NotNull long chatId) {
        log.debug("Received request for story by query [{}] wirh user [{}]", storyQuery, chatId);

        Optional<Story> story = ofNullable(getStoryUnseen(storyQuery, chatId));

        log.debug("Received story [{}] by query [{}] with user [{}]", story, storyQuery, chatId);

        story.ifPresent(story_ -> storyRepository.markStoryAsSeen(story_.getId(), chatId));

        return story;
    }

    private Story getStoryUnseen(@NotNull StoryQuery storyQuery, @NotNull long chatId) {
        Length length = storyQuery.getLength();

        if (storyQuery.getType() == PART) {
            storyQuery.setLength(null);
        }

        Story story = storyRepository.getStoryUnseen(storyQuery, chatId);
        if (story != null && storyQuery.getType() == PART) {
            story.setText(cutStoryPartToLength(story.getText(), length));
        }

        return story;
    }

    private String cutStoryPartToLength(String text, Length length) {
        char[] chars = text.toCharArray();

        int wordCount = 0;
        StringBuilder cuttedText = new StringBuilder();

        for (char nextChar : chars) {
            if (isWhitespace(nextChar)) {
                wordCount++;
            }
            cuttedText.append(nextChar);
            if (wordCount == length.getWordsNumberRange().upperEndpoint()) {
                break;
            }
        }

        return cuttedText.toString();
    }
}