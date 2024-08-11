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
@Order(2)
public class ListGuteNachtCustomizeCommand extends SimpleCommand {

    private final GuteNachtService guteNachtService;

    public ListGuteNachtCustomizeCommand(GuteNachtService guteNachtService) {
        super("/customizes", false);
        this.guteNachtService = guteNachtService;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.sendListCustomizesToUser(user);
    }
}
