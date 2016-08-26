package ru.skuptsov.telegram.bot.goodstory.repository;

import ru.skuptsov.telegram.bot.goodstory.model.dialog.UserDialog;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public interface UserDialogStore {

    UserDialog startUserDialog(Long chatId);

    UserDialog getUserDialog(Long chatId);

    void updateUserDialog(Long chatId, UserDialog userDialog);

    void finishUserDialog(Long chatId);
}
