package ru.skuptsov.telegram.bot.goodstory.repository;

import ru.skuptsov.telegram.bot.goodstory.model.dialog.StoryUserDialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface UserDialogStore {

    StoryUserDialog startUserDialog(Long chatId, StoryUserDialog.Type type);

    StoryUserDialog getUserDialog(Long chatId);

    void updateUserDialog(Long chatId, StoryUserDialog storyUserDialog);

    void finishUserDialog(Long chatId);
}
