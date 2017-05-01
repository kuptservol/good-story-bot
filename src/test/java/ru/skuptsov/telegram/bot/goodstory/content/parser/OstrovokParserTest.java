package ru.skuptsov.telegram.bot.goodstory.content.parser;

import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.skuptsov.telegram.bot.goodstory.TestConfiguration;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;

import java.io.IOException;

@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                GoodStoryBotConfiguration.class,
                TestConfiguration.class
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class OstrovokParserTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OstrovokParser ostrovokParser;

    @Test
    public void testParse() throws IOException {
        ostrovokParser.parse("http://ostrovok.de/c/classic/page/");
    }
}