package ru.skuptsov.telegram.bot.goodstory.processor;

import com.google.common.primitives.Ints;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.SubscribeService;
import ru.skuptsov.telegram.bot.platform.client.command.Reply;
import ru.skuptsov.telegram.bot.platform.client.command.ReplyTo;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageHandler;
import ru.skuptsov.telegram.bot.platform.handler.annotation.MessageMapping;
import ru.skuptsov.telegram.bot.platform.model.UpdateEvent;

import static ru.skuptsov.telegram.bot.goodstory.processor.DialogUtils.getChatId;
import static ru.skuptsov.telegram.bot.goodstory.processor.UnsubscribeDialogProcessor.UNSUBSCRIBE;

@MessageHandler
public class SubscribeDialogProcessor {

    public static final String WRONG_FORMAT = "Неправильный формат - задайте час дня от 0 до 23";
    public static final String SUCCESS_NEW_SUBCRIPTION =
            "Вы успешно подписались. В указанный вами час каждый день будет приходить новый рассказ.\n\n" +
                    "Отписаться можно, выбрав команду " + UNSUBSCRIBE + ".";
    public static final String SUCCESS_UPDATE_SUBCRIPTION =
            "Ваша подписка успешно обновлена. В указанный вами час каждый день будет приходить новый рассказ.\n\n" +
                    "Отписаться можно, выбрав команду " + UNSUBSCRIBE + ".";

    @Autowired
    private UserDialogStore userDialogStore;

    @Autowired
    private SubscribeService subscribeService;

    @MessageMapping(regexp = ".*")
    public Reply subscribe(UpdateEvent updateEvent) {
        Long chatId = getChatId(updateEvent);
        StoryUserDialog userDialog = userDialogStore.getUserDialog(chatId);
        if (userDialog.isAwaitingSubscriptionTime()) {
            String message = tryTosubscribe(updateEvent, chatId);
            return ReplyTo.to(chatId.toString()).withMessage(message);
        }

        return Reply.EMPTY;
    }

    private String tryTosubscribe(UpdateEvent updateEvent, Long chatId) {
        String messageText = updateEvent.getUpdate().getMessage().getText();

        Integer hour = validateAndGetHour(messageText);
        if (hour == null) {
            return WRONG_FORMAT;
        }

        String message;
        if (subscribeService.get(chatId).isPresent()) {
            subscribeService.remove(chatId);
            message = SUCCESS_UPDATE_SUBCRIPTION;
        } else {
            message = SUCCESS_NEW_SUBCRIPTION;
        }

        subscribeService.add(
                new LocalTime(hour, 0, 0, 0),
                null,
                userDialogStore.getUserDialog(chatId).getStoryQuery(),
                chatId);

        userDialogStore.finishUserDialog(chatId);

        return message;
    }

    private Integer validateAndGetHour(String messageText) {
        if (StringUtils.isEmpty(messageText)) {
            return null;
        }
        Integer hour = Ints.tryParse(messageText);
        if (hour != null && hour <= 23 && hour >= 0) {
            return hour;
        }

        return null;
    }
}
