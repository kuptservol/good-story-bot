package ru.skuptsov.telegram.bot.goodstory.model;

import com.google.common.base.MoreObjects;
import lombok.*;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Entity
@Table(name = "story")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String text;

//    private LocalDate created;

    @NotNull
    private String author;

    @NotNull
    private Integer rating;

    @NotNull
    private Integer genre;

    @NotNull
    private Integer length;

    @NotNull
    private Integer type;

    @NotNull
    private Integer language;
}
