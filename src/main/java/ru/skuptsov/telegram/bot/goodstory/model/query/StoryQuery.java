package ru.skuptsov.telegram.bot.goodstory.model.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.*;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@ToString
@Getter
@Setter
public class StoryQuery {

    private Type type;
    private Genre genre;
    private Length length;
    private Sorting sorting;
    private Language language;
}
