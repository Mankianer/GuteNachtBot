package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class TelegramUserComponent {

    private final TelegramUserRepo telegramUserRepo;
    @Setter
    private TelegramService telegramService;
    @Setter
    private TelegramBot telegramBot;

    @Value("${telegram.bot.admin}")
    private String adminUsername;

    public TelegramUserComponent(TelegramUserRepo telegramUserRepo) {
        this.telegramUserRepo = telegramUserRepo;
    }

    @PostConstruct
    public void init() {
        log.debug("TelegramUserComponent started");
        if (telegramUserRepo.findByUsername(adminUsername).isEmpty()) {
            TelegramUser user = new TelegramUser();
            user.setUsername(adminUsername);
            user.setState(TelegramUser.State.ADMIN);
            telegramUserRepo.save(user);
            log.warn("Create default admin user in Database: {}", user);
        }
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
    public TelegramUser getUserByUpdate(Update update) {
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

    public void handleNewUser(TelegramUser user) {
            sendApprovalMessageToAdmin(user);
            telegramService.sendMessage("Hallo %s, bitte warte bis dein Account durch ein Admin verifiziert wurde.".formatted(user.getFirstname()), user);
    }

    public void sendApprovalMessageToAdmin(TelegramUser user) {
        KeyboardRow row = new KeyboardRow();
        row.add("Approve %s".formatted(user.getId()));
        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup(List.of(row));
        sendMessageToAdmins(SendMessage.builder().chatId(adminUsername).text("Neuer User:%n%s".formatted(user))
                .replyMarkup(replyMarkup).build());
    }

    public void sendMessageToAdmins(SendMessage message) {
        telegramUserRepo.findByStateAndChatIdNotNull(TelegramUser.State.ADMIN).forEach(user -> {
            message.setChatId(String.valueOf(user.getChatId()));
            telegramService.sendMessage(message);
        });
    }


}
