package de.mankianer.gutenachtbot.telegram.component;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.TelegramUserRepo;
import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Log4j2
@Component
public class TelegramUserComponent {

    @Setter
    private TelegramService telegramService;
    private final TelegramUserRepo telegramUserRepo;
    private final TelegramAdminComponent telegramAdminComponent;

    public TelegramUserComponent(TelegramUserRepo telegramUserRepo, TelegramAdminComponent telegramAdminComponent) {
        this.telegramUserRepo = telegramUserRepo;
        this.telegramAdminComponent = telegramAdminComponent;
    }

    /**
     * Get user by Telegram Api Message
     * -> search for user by chatId and Username
     * -> If chatId is not set, set chatId from message and save user
     * -> If user is not in database, create temporary user
     * -> add User first name from message
     *
     * @param update
     * @return
     */
    TelegramUser getUserByUpdate(Update update) {
        long chatId = update.getMessage().getChat().getId();
        String username = update.getMessage().getChat().getUserName();
        Optional<TelegramUser> foundUser = telegramUserRepo.findFirstByChatIdOrUsername(chatId, username);
        TelegramUser telegramUser = foundUser.orElseGet(() -> {
            TelegramUser user = new TelegramUser();
            user.setUsername(username);
            return user;
        });
        telegramUser.setFirstname(update.getMessage().getChat().getFirstName());
        if (telegramUser.getChatId() == null) {
            log.info("new chatId for user: {}", telegramUser);
            telegramUser.setChatId(chatId);
            telegramUserRepo.save(telegramUser);
        }

        return telegramUser;
    }

    void handleNewUser(TelegramUser user) {
        telegramAdminComponent.sendApprovalMessageToAdmin(user);
        telegramService.sendMessage("Hallo %s, bitte warte bis dein Account durch ein Admin verifiziert wurde.".formatted(user.getFirstname()), user);
    }

}
