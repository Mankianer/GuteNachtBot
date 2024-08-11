package de.mankianer.gutenachtbot.telegram.components;

import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.TelegramUserRepo;
import de.mankianer.gutenachtbot.telegram.commands.CommandInterface;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class TelegramAdminComponent {
    @Setter
    private TelegramService telegramService;
    private final TelegramUserRepo telegramUserRepo;
    @Value("${telegram.bot.admin}")
    private String adminUsername;
    @Getter
    private List<CommandInterface> adminCommands;

    public TelegramAdminComponent(TelegramUserRepo telegramUserRepo) {
        this.telegramUserRepo = telegramUserRepo;
        adminCommands = new ArrayList<>();
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

    void sendApprovalMessageToAdmin(TelegramUser user) {
        KeyboardRow row = new KeyboardRow();
        row.add("/approve %s".formatted(user.getId()));
        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup(List.of(row));
        sendMessageToAdmins(SendMessage.builder().chatId("").text("Neuer User:%n%s".formatted(user))
                .replyMarkup(replyMarkup).build());
    }

    public void sendMessageToAdmins(SendMessage message) {
        telegramUserRepo.findByStateAndChatIdNotNull(TelegramUser.State.ADMIN).forEach(user -> {
            message.setChatId(String.valueOf(user.getChatId()));
            telegramService.sendMessage(message);
        });
    }

    public void registerCommand(CommandInterface command) {
        adminCommands.add(command);
    }

    public void approveUser(TelegramUser user) {
        user.setState(TelegramUser.State.VERIFIED);
        telegramUserRepo.save(user);
        sendMessageToAdmins(SendMessage.builder().chatId(user.getChatId()).text("User %s approved.".formatted(user)).build());
        telegramService.sendMessage(telegramService.getWelcomeMessage(user), user);
    }
}
