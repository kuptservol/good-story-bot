package ru.skuptsov.telegram.bot.goodstory.service.subscribe.impl;

import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.skuptsov.telegram.bot.goodstory.TestConfiguration;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;
import ru.skuptsov.telegram.bot.goodstory.model.Subscription;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.SubscribeService;

import java.util.Optional;

@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                GoodStoryBotConfiguration.class,
                TestConfiguration.class
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class SubscribeServiceImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SubscribeService subscribeService;

    @Test
    public void test() {
        int userId = 1;
        subscribeService.add(
                "* * * * *",
                StoryQuery.builder()
                        .type(Type.STORY)
                        .length(Length.ONE_MINUTE)
                        .build(),
                userId);

        Optional<Subscription> subscription = subscribeService.get(userId);

        subscribeService.remove(userId);

        subscription = subscribeService.get(userId);

        System.out.println(subscription);
    }
}