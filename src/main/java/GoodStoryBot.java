import org.springframework.boot.SpringApplication;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;

/**
 * @author Sergey Kuptsov
 * @since 21/05/2016
 */
public class GoodStoryBot {

    public static void main(String[] args) {
        SpringApplication.run(GoodStoryBotConfiguration.class, args);
    }
}
