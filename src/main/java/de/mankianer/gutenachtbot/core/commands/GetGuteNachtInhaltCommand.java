package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GetGuteNachtInhaltCommand extends SimpleCommand {

    private final GuteNachtService guteNachtService;

    public GetGuteNachtInhaltCommand(GuteNachtService guteNachtService) {
        super("/inhalt");
        this.guteNachtService = guteNachtService;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.getGuteNachtInhalt(user);
    }
}
