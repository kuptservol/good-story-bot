package ru.skuptsov.telegram.bot.goodstory.parser;


import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.skuptsov.telegram.bot.goodstory.TestConfiguration;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                TestConfiguration.class,
                GoodStoryBotConfiguration.class
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class StorySaverTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StorySaver storySaver;

    @Test
    public void save() {
        Path path = Paths.get("books/Мертвые души. Том первый@Н.В. Гоголь@1842.txt");

        storySaver.processFile(path);
    }
}
