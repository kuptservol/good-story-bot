package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Repository;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.UserDialog;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@Repository
public class UserDialogStoreImpl implements UserDialogStore {

    private final Cache<Long, UserDialog> userDialogCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    @Override
    public UserDialog startUserDialog(Long chatId) {
        UserDialog dialog = UserDialog.builder()
                .dialogState(DialogState.START)
                .storyQuery(new StoryQuery())
                .build();
        userDialogCache.put(chatId,
                dialog);

        return dialog;
    }

    @Override
    public UserDialog getUserDialog(Long chatId) {
        UserDialog userDialog = userDialogCache.getIfPresent(chatId);
        if (userDialog == null) {
            UserDialog dialog = UserDialog.builder()
                    .dialogState(DialogState.START)
                    .storyQuery(new StoryQuery())
                    .build();
            userDialogCache.put(chatId, dialog);
            return dialog;
        } else {
            return userDialog;
        }
    }

    @Override
    public void updateUserDialog(Long chatId, UserDialog userDialog) {
        userDialogCache.put(chatId, userDialog);
    }

    @Override
    public void finishUserDialog(Long chatId) {
        userDialogCache.invalidate(chatId);
    }
}
