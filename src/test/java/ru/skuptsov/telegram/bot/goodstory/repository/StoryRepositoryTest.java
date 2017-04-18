package ru.skuptsov.telegram.bot.goodstory.repository;

import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import ru.skuptsov.telegram.bot.goodstory.TestConfiguration;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Genre;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Language;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                GoodStoryBotConfiguration.class,
                TestConfiguration.class
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class StoryRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StoryRepository storyRepository;

    @Test
    public void getStoryUnseen() {
        Story storyUnseen = storyRepository.getStoryUnseen(
                StoryQuery.builder()
                        .genre(Genre.ANY)
                        .length(Length.ONE_MINUTE)
                        .type(Type.STORY)
                        .build(), 2);

        System.out.println(storyUnseen);
    }

    @Test
    public void save() {
        storyRepository.add(
                Story.builder()
                        .type(Type.STORY)
                        .length(Length.ONE_MINUTE)
                        .author("Джей Рип")
                        .text("Был только один выход, ибо наши жизни сплелись в слишком запутанный узел гнева и блаженства, чтобы решить все как-нибудь иначе. Доверимся жребию: орел — и мы поженимся, решка — и мы расстанемся навсегда.\n" +
                                "Монетка была подброшена. Она звякнула, завертелась и остановилась. Орел.\n" +
                                "Мы уставились на нее с недоумением.\n" +
                                "Затем, в один голос, мы сказали: «Может, еще разок?»")
                        .name("Судьба")
                        .language(Language.RUSSIAN)
                        .genre(Genre.DRAMA)
                        .build());
    }

    @Test
    public void markStoryAsSeen() {
        storyRepository.markStoryAsSeen(5, 1);
    }
}
