package de.mankianer.gutenachtbot.telegram.commands.admin;

import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.TelegramUserRepo;
import de.mankianer.gutenachtbot.telegram.components.TelegramAdminComponent;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminAprove extends SimpleCommand {

    public static final String COMMAND_NAME = "/approve";
    private final TelegramAdminComponent telegramAdminComponent;
    private final TelegramUserRepo telegramUserRepo;

    public AdminAprove(TelegramAdminComponent telegramAdminComponent, TelegramUserRepo telegramUserRepo) {
        super(COMMAND_NAME, true);
        this.telegramAdminComponent = telegramAdminComponent;
        this.telegramUserRepo = telegramUserRepo;
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        String arg = update.getMessage().getText().substring(COMMAND_NAME.length() + 1);
        long userId = Long.parseLong(arg);
        telegramUserRepo.findById(userId).ifPresent(telegramAdminComponent::approveUser);
    }
}
