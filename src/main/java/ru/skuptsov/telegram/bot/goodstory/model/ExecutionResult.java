package ru.skuptsov.telegram.bot.goodstory.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Getter
@Setter
public class ExecutionResult<T> {

    private boolean ok;
    private T result;
    private String description;
    private Integer error_code;
}
