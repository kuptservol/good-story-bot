package ru.skuptsov.telegram.bot.goodstory.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.goodstory.processor.EventProcessor;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Getter
@ToString
@NoArgsConstructor
@Builder
public class UpdateEvent {
    public final static UpdateEvent EMPTY = new UpdateEvent();

    private Update update;
    private EventProcessor eventProcessor;
    private DateTime received;
}
