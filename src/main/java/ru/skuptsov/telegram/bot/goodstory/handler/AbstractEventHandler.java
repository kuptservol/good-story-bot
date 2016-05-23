package ru.skuptsov.telegram.bot.goodstory.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skuptsov.telegram.bot.goodstory.model.UpdateEvent;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public abstract class AbstractEventHandler implements EventHandler {
    private final static Logger logger = LoggerFactory.getLogger(AbstractEventHandler.class);

    private AbstractEventHandler next;

    @Override
    public void handleEvent(@NotNull UpdateEvent updateEvent) {
        logger.debug("Executing next handler {} with message {}", this.getClass().getName(), updateEvent);
        try {
            handle(updateEvent);
        } catch (Exception e) {
            logger.error("Error occured while handling message {} with exception {}", updateEvent, e);
        }

        handleWithNextHandler(updateEvent);
    }

    public EventHandler next(AbstractEventHandler handler) {
        setNext(handler);
        return this;
    }

    protected abstract void handle(@NotNull UpdateEvent updateEvent);

    protected void setNext(AbstractEventHandler messageHandler) {
        if (this.next != null) {
            next.setNext(messageHandler);
        }
    }

    private void handleWithNextHandler(UpdateEvent updateEvent) {
        if (next != null) {
            next.handleEvent(updateEvent);
        }
    }
}
