package ru.skuptsov.telegram.bot.goodstory.model;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Story {
    private Long id;
    private String text;
    private LocalDate created;
    private String author;
    private Integer rating;
    private Integer genre;
    private Integer length;
    private Integer type;
    private Integer language;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("text", "{text}")
                .add("created", created)
                .add("author", author)
                .add("rating", rating)
                .add("genre", genre)
                .add("length", length)
                .add("type", type)
                .add("language", language)
                .toString();
    }
}
