package ru.skuptsov.telegram.bot.goodstory.model;

import com.google.common.base.MoreObjects;
import lombok.*;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Genre;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Language;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;

import javax.persistence.*;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "story")
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Length length;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String text;

    private String name;

    private Integer year;

    private String author;

    private Integer rating;

    private Integer part;

    private String link;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("text", "{text}")
                .add("year", year)
                .add("author", author)
                .add("rating", rating)
                .add("genre", genre)
                .add("length", length)
                .add("type", type)
                .add("language", language)
                .add("link", link)
                .add("part", part)
                .toString();
    }
}
