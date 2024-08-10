package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtConfigRepo;
import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.CommandInterface;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GetGuteNachtInhaltCommand implements CommandInterface {

    private final GuteNachtService guteNachtService;

    public GetGuteNachtInhaltCommand(GuteNachtService guteNachtService) {
        this.guteNachtService = guteNachtService;
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return message.toLowerCase().startsWith("/inhalt");
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        guteNachtService.getGuteNachtInhalt(user);
    }
}
