package ru.skuptsov.telegram.bot.goodstory.content.admin;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class CreateBookRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String author;
    @NotEmpty
    private String text;

    private String year;
}
