package ru.skuptsov.telegram.bot.platform.processor.resolver;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.ConditionEventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
public class ConditionEventProcessResolver extends AbstractProcessorResolver {

    private final List<ConditionEventProcessor> conditionEventProcessors;

    public ConditionEventProcessResolver(@NotNull List<ConditionEventProcessor> conditionEventProcessors) {
        this.conditionEventProcessors = conditionEventProcessors;
    }

    @Override
    protected EventProcessor resolveProcessor(UpdateEvent updateEvent) {
        return conditionEventProcessors.stream()
                .filter(conditionEventProcessor -> conditionEventProcessor.isSuitableForProcessingEvent(updateEvent))
                .findFirst()
                .map(conditionEventProcessor -> (EventProcessor) conditionEventProcessor)
                .orElse(EventProcessor.EMPTY);
    }
}
