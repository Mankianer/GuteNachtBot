package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.Getter;

public abstract class SimpleCommand implements CommandInterface {

    @Getter
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
