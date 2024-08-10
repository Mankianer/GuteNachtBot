package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

public abstract class SimpleCommand implements CommandInterface {

    private final String command;
    @Getter
    private final boolean isAdminCommand;

    public SimpleCommand(String command) {
        this(command, false);
    }

    public SimpleCommand(String command, boolean isAdminCommand) {
        this.command = command;
        this.isAdminCommand = isAdminCommand;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.startsWith(command);
    }
}
