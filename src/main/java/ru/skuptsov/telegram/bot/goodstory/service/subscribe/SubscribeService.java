package ru.skuptsov.telegram.bot.goodstory.service.subscribe;

import org.joda.time.LocalTime;
import ru.skuptsov.telegram.bot.goodstory.model.Subscription;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface SubscribeService {

    void add(@NotNull String cron, @NotNull StoryQuery storyQuery, long chatId);

    Optional<Subscription> get(long chatId);

    void remove(long chatId);

    void add(@NotNull LocalTime localTime,
             @Nullable Location location,
             @NotNull StoryQuery storyQuery,
             long chatId);
}
