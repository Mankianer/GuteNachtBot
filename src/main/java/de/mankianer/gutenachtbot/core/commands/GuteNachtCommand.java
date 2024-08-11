package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GuteNachtCommand extends SimpleCommand {

    private final GuteNachtService guteNachtService;

    public GuteNachtCommand(GuteNachtService guteNachtService) {
        super("gute nacht", "Zum Triggern der GuteNacht Nachricht für den heutigen Tag.");
        this.guteNachtService = guteNachtService;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.sendGuteNacht(user);
        guteNachtService.rescheduleGuteNacht(user);
    }
}
