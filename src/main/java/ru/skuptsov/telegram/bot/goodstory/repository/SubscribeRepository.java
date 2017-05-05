package ru.skuptsov.telegram.bot.goodstory.repository;

import ru.skuptsov.telegram.bot.goodstory.model.Subscription;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface SubscribeRepository {
    void save(@NotNull Subscription subscription);

    void delete(@NotNull Subscription subscription);

    Optional<Subscription> get(long userId);

    List<Subscription> getAll();
}
