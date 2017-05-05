package ru.skuptsov.telegram.bot.goodstory.processor;

import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

public class DialogUtils {
    public static Long getChatId(UpdateEvent updateEvent) {
        return updateEvent.getUpdate().getMessage().getChatId();
    }

    public static Long getChatIdFromCallback(UpdateEvent updateEvent) {
        return updateEvent.getUpdate().getCallbackQuery().getMessage().getChatId();
    }

    public static Integer getUserIdFromCallback(UpdateEvent updateEvent) {
        return updateEvent.getUpdate().getCallbackQuery().getMessage().getFrom().getId();
    }
}
