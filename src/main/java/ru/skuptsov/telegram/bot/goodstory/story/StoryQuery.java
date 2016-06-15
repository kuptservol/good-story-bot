package ru.skuptsov.telegram.bot.goodstory.story;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skuptsov.telegram.bot.goodstory.dialog.Genre;
import ru.skuptsov.telegram.bot.goodstory.dialog.Language;
import ru.skuptsov.telegram.bot.goodstory.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.dialog.Sorting;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@ToString
@Getter
@Setter
public class StoryQuery {

    private Genre genre;
    private Length length;
    private Sorting sorting;
    private Language language;
}
