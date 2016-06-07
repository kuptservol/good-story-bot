package ru.skuptsov.telegram.bot.platform.model;

import lombok.*;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEvent {
    public final static UpdateEvent EMPTY = new UpdateEvent();

    private Update update;
    private EventProcessor eventProcessor;
    private DateTime received;
}
