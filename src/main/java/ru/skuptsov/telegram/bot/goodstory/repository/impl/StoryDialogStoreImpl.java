package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Repository;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.DialogState;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.UserDialogStore;

import java.util.concurrent.TimeUnit;

import static ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog.Type.READ;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
@Repository
public class StoryDialogStoreImpl implements UserDialogStore {

    private final Cache<Long, StoryUserDialog> userDialogCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    @Override
    public StoryUserDialog startUserDialog(Long chatId, StoryUserDialog.Type type) {
        StoryUserDialog dialog = StoryUserDialog.builder()
                .dialogState(DialogState.START)
                .storyQuery(StoryQuery.builder().build())
                .type(type)
                .build();
        userDialogCache.put(chatId,
                dialog);

        return dialog;
    }

    @Override
    public StoryUserDialog getUserDialog(Long chatId) {
        StoryUserDialog storyUserDialog = userDialogCache.getIfPresent(chatId);
        if (storyUserDialog == null) {
            StoryUserDialog dialog = StoryUserDialog.builder()
                    .dialogState(DialogState.START)
                    .type(READ)
                    .storyQuery(StoryQuery.builder().build())
                    .build();
            userDialogCache.put(chatId, dialog);
            return dialog;
        } else {
            return storyUserDialog;
        }
    }

    @Override
    public void updateUserDialog(Long chatId, StoryUserDialog storyUserDialog) {
        userDialogCache.put(chatId, storyUserDialog);
    }

    @Override
    public void finishUserDialog(Long chatId) {
        userDialogCache.invalidate(chatId);
    }
}
