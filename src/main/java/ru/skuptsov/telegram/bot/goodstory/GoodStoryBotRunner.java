package ru.skuptsov.telegram.bot.goodstory;

import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;

import static ru.skuptsov.telegram.bot.platform.config.BotPlatformStarter.start;

/**
 * @author Sergey Kuptsov
 * @since 04/07/2016
 */
public class GoodStoryBotRunner {
    public static void main(String[] args) {
        start(GoodStoryBotConfiguration.class, args);
    }
}
