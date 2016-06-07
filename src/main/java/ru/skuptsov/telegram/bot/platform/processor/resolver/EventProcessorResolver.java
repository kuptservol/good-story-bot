package ru.skuptsov.telegram.bot.platform.processor.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.CommandEventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.ConditionEventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.DefaultEventProcessor;
import ru.skuptsov.telegram.bot.platform.processor.EventProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Component
public class EventProcessorResolver {
    private final Logger log = LoggerFactory.getLogger(EventProcessorResolver.class);

    @Autowired(required = false)
    private List<CommandEventProcessor> commandEventProcessors;

    @Autowired(required = false)
    private List<ConditionEventProcessor> conditionEventProcessors = new ArrayList<>();

    @Autowired(required = false)
    private DefaultEventProcessor defaultEventProcessor;

    private ProcessorResolver processorResolver;

    public EventProcessor resolve(UpdateEvent updateEvent) {
        log.debug("Resolving processor for event {}", updateEvent);

        return processorResolver.resolve(updateEvent);
    }

    @PostConstruct
    public void init() {
        Map<String, EventProcessor> commandEventProcessorMap = new HashMap<>();

        commandEventProcessors.stream().forEach(commandEventProcessor -> {
            List<String> commandTextList = commandEventProcessor.getCommandText();

            for (String commandText : commandTextList) {

                if (isEmpty(commandText)) {
                    log.error("Found empty command in {}", commandEventProcessor);
                    throw new IllegalArgumentException("Found empty command");
                }

                if (commandEventProcessorMap.containsKey(commandText)) {
                    log.error("Duplicate command [{}] configuration found in processor {}", commandText, commandEventProcessor);
                    throw new IllegalArgumentException("Duplicate command configuration found in processor");
                }

                commandEventProcessorMap.put(commandText, commandEventProcessor);
            }
        });


        processorResolver = new CommandEventProcessorResolver(commandEventProcessorMap);
        ConditionEventProcessResolver conditionEventProcessResolver = new ConditionEventProcessResolver(conditionEventProcessors);
        DefaultEventProcessorResolver defaultEventProcessorResolver = new DefaultEventProcessorResolver(defaultEventProcessor);

        processorResolver.setNext(conditionEventProcessResolver).setNext(defaultEventProcessorResolver);
    }
}
