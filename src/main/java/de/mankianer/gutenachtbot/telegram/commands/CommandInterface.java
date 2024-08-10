package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandInterface {

    /**
     *
     * @param message
     * @return true if the message matches the command
     */
    boolean matchesMessage(String message, TelegramUser user);

    void onExecute(Update update, TelegramUser user);

    boolean isAdminCommand();
}
