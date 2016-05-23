package ru.skuptsov.telegram.bot.goodstory.client.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.goodstory.client.NextOffsetStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Component
public class InMemoryNextOffsetStrategyImpl implements NextOffsetStrategy {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Integer getNextOffset() {
        return counter.incrementAndGet();
    }

    @Override
    public void saveCurrentOffset(List<Update> updates) {
        updates.stream().map(Update::getUpdateId).max(Integer::compareTo).ifPresent(counter::set);
    }
}
