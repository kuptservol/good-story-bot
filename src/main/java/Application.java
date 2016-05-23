import org.springframework.boot.SpringApplication;
import ru.skuptsov.telegram.bot.goodstory.config.ApplicationConfiguration;

/**
 * @author Sergey Kuptsov
 * @since 21/05/2016
 */
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfiguration.class, args);
    }
}
