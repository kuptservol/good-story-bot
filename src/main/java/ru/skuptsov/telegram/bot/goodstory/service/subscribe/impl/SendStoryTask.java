package ru.skuptsov.telegram.bot.goodstory.service.subscribe.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.processor.story.StoryTextBuilder;
import ru.skuptsov.telegram.bot.goodstory.service.story.StoryService;
import ru.skuptsov.telegram.bot.platform.client.TelegramBotApi;

import java.io.IOException;
import java.util.Optional;

import static ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration.applicationContext;

public class SendStoryTask implements Job {
    private final Logger log = LoggerFactory.getLogger(SendStoryTask.class);

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static StoryQuery getStoryQueryObject(String storyQuery) {
        try {
            return jsonMapper.readValue(storyQuery, StoryQuery.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot parse storyquery : {" + storyQuery + "}", e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getMergedJobDataMap();
        Long chatId = Long.valueOf(map.getString("chatId"));
        StoryQuery storyQuery = getStoryQueryObject(map.getString("storyQuery"));
        try {

            StoryService storyService = applicationContext.getBean(StoryService.class);

            Optional<Story> story = storyService.getStory(storyQuery, chatId);

            if (story.isPresent()) {
                TelegramBotApi telegramBotApi = applicationContext.getBean(TelegramBotApi.class);
                StoryTextBuilder storyTextBuilder = applicationContext.getBean(StoryTextBuilder.class);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(storyTextBuilder.build(story));
                telegramBotApi.sendMessage(sendMessage).async();
            } else {
                log.warn("No new stories for args chatId : {}, storyQuery : {}, can't send");
            }
        } catch (Exception e) {
            log.error("Error while sending sheduled story with args chatId : {}, storyQuery : {}", e);
        }
    }
}
