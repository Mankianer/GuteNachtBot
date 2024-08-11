package de.mankianer.gutenachtbot.telegram.commands;

import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class SimpleCommand implements CommandInterface {

    @Getter
    private final String command;
    private String description = "N/A";
    @Getter
    private final boolean isAdminCommand;

    public SimpleCommand(String command) {
        this(command, false);
    }

    public SimpleCommand(String command, String description) {
        this(command, description, false);
    }

    public SimpleCommand(String command, boolean isAdminCommand) {
        this(command, "N/A", isAdminCommand);
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.toLowerCase().startsWith(command.toLowerCase());
    }

    @Override
    public String getDescription() {
        return command + " - " + description;
    }
}
