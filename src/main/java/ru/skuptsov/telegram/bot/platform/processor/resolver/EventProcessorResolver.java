package ru.skuptsov.telegram.bot.platform.processor.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;
import ru.skuptsov.telegram.bot.platform.processor.*;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Sergey Kuptsov
 * @since 31/05/2016
 */
@Component
public class EventProcessorResolver implements ProcessorResolver {
    private final Logger log = LoggerFactory.getLogger(EventProcessorResolver.class);

    @Autowired(required = false)
    private List<MessageTextEventProcessor> messageTextEventProcessors;

    @Autowired(required = false)
    private List<CallbackQueryDataEventProcessor> callbackQueryDataEventProcessors = new ArrayList<>();

    @Autowired(required = false)
    private List<ConditionEventProcessor> conditionEventProcessors = new ArrayList<>();

    @Autowired(required = false)
    private DefaultEventProcessor defaultEventProcessor;

    private ProcessorResolver processorResolver;

    @Override
    public EventProcessor resolve(UpdateEvent updateEvent) {
        log.debug("Resolving processor for event {}", updateEvent);

        return processorResolver.resolve(updateEvent);
    }

    //todo: go to new interface
    @Override
    public ProcessorResolver setNext(ProcessorResolver processorResolver) {
        return null;
    }

    @PostConstruct
    public void init() {
        Map<Object, EventProcessor> messageTextProcessorMap = initializeMessageEventProcessor();
        Map<Object, EventProcessor> callbackDataEventProcessorMap = initializeCallbackDataEventProcessor();

        processorResolver = new MessageTextProcessorResolver(messageTextProcessorMap);
        CallbackQueryDataProcessorResolver callbackQueryDataProcessorResolver = new CallbackQueryDataProcessorResolver(callbackDataEventProcessorMap);
        ConditionEventProcessResolver conditionEventProcessResolver = new ConditionEventProcessResolver(conditionEventProcessors);
        DefaultEventProcessorResolver defaultEventProcessorResolver = new DefaultEventProcessorResolver(defaultEventProcessor);

        processorResolver
                .setNext(callbackQueryDataProcessorResolver)
                .setNext(conditionEventProcessResolver)
                .setNext(defaultEventProcessorResolver);
    }

    private Map<Object, EventProcessor> initializeMessageEventProcessor() {
        Map<Object, EventProcessor> messageTextProcessorMap = new HashMap<>();

        messageTextEventProcessors.stream().forEach(commandEventProcessor -> {
            Set<String> commandTextList = commandEventProcessor.getMessageText();

            fillProcessorsMap(messageTextProcessorMap, commandEventProcessor, commandTextList);

        });
        return messageTextProcessorMap;
    }

    private Map<Object, EventProcessor> initializeCallbackDataEventProcessor() {
        Map<Object, EventProcessor> callbackDataEventProcessorMap = new HashMap<>();

        callbackQueryDataEventProcessors.stream().forEach(callbackQueryProcessor -> {
            Set<String> callbackQueryDataList = callbackQueryProcessor.getCallbackQueryData();

            fillProcessorsMap(callbackDataEventProcessorMap, callbackQueryProcessor, callbackQueryDataList);
        });
        return callbackDataEventProcessorMap;
    }

    private void fillProcessorsMap(Map<Object, EventProcessor> eventProcessorsMap,
                                   EventProcessor eventProcessor,
                                   Set<String> dataList) {
        for (String data : dataList) {

            if (isEmpty(data)) {
                log.error("Found empty command in {}", eventProcessor);
                throw new IllegalArgumentException("Found empty command");
            }

            if (eventProcessorsMap.containsKey(data)) {
                log.error("Duplicate command [{}] configuration found in processor {}", data, eventProcessor);
                throw new IllegalArgumentException("Duplicate command configuration found in processor");
            }

            eventProcessorsMap.put(data, eventProcessor);
        }
    }
}
