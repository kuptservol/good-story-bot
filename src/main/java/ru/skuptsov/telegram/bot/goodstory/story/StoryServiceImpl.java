package ru.skuptsov.telegram.bot.goodstory.story;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@Service
public class StoryServiceImpl implements StoryService {
    private final static Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    @Override
    public String getStory(StoryQuery storyQuery) {
        log.debug("Received request for story by query [{}]", storyQuery);

        return "Говорят, зло не имеет лица. Действительно, " +
                "на его лице не отражалось никаких чувств. " +
                "Ни проблеска сочувствия не было на нем, " +
                "а ведь боль просто невыносима. " +
                "Разве он не видит ужас в моих глазах и панику на моем лице? " +
                "Он спокойно, можно сказать, " +
                "профессионально выполнял свою грязную работу, " +
                "а в конце учтиво сказал: «Прополощите рот, пожалуйста»";
    }
}
