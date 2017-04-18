package ru.skuptsov.telegram.bot.goodstory.model.query;

import lombok.*;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.*;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class StoryQuery {
    private Type type;
    private Length length;
    // Disabled
    private Genre genre;
    // Disabled
    private Sorting sorting;
    // Disabled
    private Language language;
}
