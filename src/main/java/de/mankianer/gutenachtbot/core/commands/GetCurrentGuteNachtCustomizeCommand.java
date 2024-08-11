package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Command to save a GuteNachtCustomize
 * /customize_remove <name>
 */
@Component
@Order(3)
public class GetCurrentGuteNachtCustomizeCommand extends SimpleCommand {

    private final GuteNachtService guteNachtService;

    public GetCurrentGuteNachtCustomizeCommand(GuteNachtService guteNachtService) {
        super("/customize", "Zum Abfragen des aktuellen Customize",false);
        this.guteNachtService = guteNachtService;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.sendCurrentCustomizeToUser(user);
    }
}
