package de.mankianer.gutenachtbot.telegram;

import de.mankianer.gutenachtbot.telegram.model.TelegramUser;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Optional;
import java.util.function.Supplier;

@Log4j2
@Component
public class TelegramService extends TelegramLongPollingBot {

    private final TelegramUserRepo telegramUserRepo;
    @Getter
    @Value("${telegram.bot.username}")
    private String botUsername;
    @Getter
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.admin}")
    private String adminUsername;



    public TelegramService(TelegramUserRepo telegramUserRepo) {
        this.telegramUserRepo = telegramUserRepo;
    }

    @PostConstruct
    public void init() {
       log.info("TelegramService started");
        if(telegramUserRepo.findByUsername(adminUsername).isEmpty()) {
            TelegramUser user = new TelegramUser();
            user.setUsername(adminUsername);
            user.setState(TelegramUser.State.ADMIN);
            telegramUserRepo.save(user);
            log.warn("Create default admin user in Database: {}", user);
        }
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Error while registering bot");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: {}", update.getMessage().getChat().getUserName());
        Optional<TelegramUser> foundUser = telegramUserRepo.findByChatId(update.getMessage().getChat().getId());
        TelegramUser telegramUser = foundUser.orElseGet(() -> {
            TelegramUser user = new TelegramUser();
            user.setUsername(update.getMessage().getChat().getUserName());
            user.setChatId(update.getMessage().getChatId());
            return user;
        });
        log.info("User: {}", telegramUser);

        foundUser.ifPresentOrElse(user -> {
            sendMessage("Hello " + user.getUsername(), user);
        }, () -> {
            sendMessage("Hello new user " + telegramUser.getUsername(), telegramUser);
            telegramUserRepo.findByUsername(telegramUser.getUsername()).forEach(user -> {
                telegramUser.setState(TelegramUser.State.ADMIN);
                telegramUserRepo.save(telegramUser);
                sendMessage("You are Admin!", telegramUser);
            });
        });

    }


    public void sendMessage(String message, TelegramUser user) {
        try {
            execute(SendMessage.builder().chatId(String.valueOf(user.getChatId())).text(message).build());
        } catch (TelegramApiException e) {
            log.error("Error while sending message", e);
        }
    }

}
