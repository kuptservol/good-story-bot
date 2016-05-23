package ru.skuptsov.telegram.bot.goodstory.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Getter
@Builder
@ToString
public class UpdateEvents {
    private List<UpdateEvent> updateEventList;
}
