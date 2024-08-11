package de.mankianer.gutenachtbot.core;

import de.mankianer.gutenachtbot.core.components.*;
import de.mankianer.gutenachtbot.core.components.exceptions.NotFoundException;
import de.mankianer.gutenachtbot.core.components.exceptions.UserNotAllowedException;
import de.mankianer.gutenachtbot.core.models.GuteNachtConfig;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.core.models.GuteNachtInhalt;
import de.mankianer.gutenachtbot.openai.OpenAIAPIService;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class GuteNachtService {

    private final TelegramService telegramService;
    private final TimerComponent timerComponent;
    private final OpenAIAPIService openAIAPIService;
    private final GuteNachtInhaltComponent guteNachtInhaltComponent;
    private final GuteNachtCustomizeComponent guteNachtCustomizeComponent;
    private final GuteNachtConfigComponent guteNachtConfigComponent;
    private final GuteNachtGeschichteComponent guteNachtGeschichteComponent;


    public GuteNachtService(TelegramService telegramService, TimerComponent timerComponent, OpenAIAPIService openAIAPIService, GuteNachtInhaltComponent guteNachtInhaltComponent, GuteNachtCustomizeComponent guteNachtCustomizeComponent, GuteNachtConfigComponent guteNachtConfigComponent, GuteNachtGeschichteComponent guteNachtGeschichteComponent) {
        this.telegramService = telegramService;
        this.timerComponent = timerComponent;
        this.guteNachtInhaltComponent = guteNachtInhaltComponent;
        this.guteNachtCustomizeComponent = guteNachtCustomizeComponent;
        this.guteNachtConfigComponent = guteNachtConfigComponent;
        this.guteNachtGeschichteComponent = guteNachtGeschichteComponent;
        this.guteNachtGeschichteComponent.setGuteNachtService(this);
        this.guteNachtCustomizeComponent.setGuteNachtService(this);
        this.timerComponent.setGuteNachtService(this);
        this.openAIAPIService = openAIAPIService;
    }

    @PostConstruct
    public void init() {

    }

    /**
     * Sends a Gute Nacht message to the user
     * Updates the nextDate in the GuteNachtConfig
     * GuteNachtConfig must be rescheduled after this method for User
     * @param user
     * @return the updated GuteNachtConfig for next Date
     */
    public GuteNachtConfig sendGuteNacht(TelegramUser user) {
        guteNachtGeschichteComponent.getGuteNachtGeschichte(user).ifPresentOrElse(story -> {
            telegramService.sendMessage(story, user);
        }, () -> telegramService.sendMessage("Es konnte keine GuteNachtGeschichte erzeugt werden. 😢", user));
        return guteNachtConfigComponent.updateGuteNachtConfigToNextDay(user);
    }

    /**
     * Reschedules the Gute Nacht message for the user
     * used to update the timercomponent
     * @param user
     */
    public void rescheduleGuteNacht(TelegramUser user) {
        timerComponent.scheduleGuteNacht(user);
    }

    /**
     * Sets the Gute Nacht time for the user and reschedules the Gute Nacht message
     * and sends a message with the next scheduled Gute Nacht message
     * @param user
     * @param time
     */
    public void setGuteNachtTime(TelegramUser user, LocalTime time) {
        GuteNachtConfig guteNachtConfig = getGuteNachtConfig(user);
        guteNachtConfig.setTimer(time);
        guteNachtConfigComponent.saveGuteNachtConfig(guteNachtConfig);
        Instant nextInstant = timerComponent.scheduleGuteNacht(user);
        if(nextInstant != null) {
            telegramService.sendMessage("Nächster GuteNachtTimer ist am %s".formatted(formatInstant(nextInstant)), guteNachtConfig.getTelegramUser());
        } else {
            telegramService.sendMessage("GuteNachtTimer ist deaktiviert", guteNachtConfig.getTelegramUser());
        }
    }

    private static String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    /**
     *
     * @param user
     * @return the GuteNachtConfig for the user, if it does not exist, a new one is created and seved
     */
    public GuteNachtConfig getGuteNachtConfig(TelegramUser user) {
        return guteNachtConfigComponent.getGuteNachtConfig(user);
    }

    /**
     * Sends the GuteNachtInhalt to the user
     * if the user is null, the message is sent to all admins
     * triggers the creation of a new GuteNachtInhalt if there is none for today
     * @param user
     */
    public void sendGuteNachtInhaltToUser(@Nullable TelegramUser user) {
        String message = guteNachtInhaltComponent.getGuteNachtInhaltToDay().map(GuteNachtInhalt::getInhalt).orElseGet(() -> {
            return "Es konnte keine Inhalt für die GuteNachtGeschichte erzeugt werden.";
        });
        if(user != null) {
            telegramService.sendMessage(message, user);
        } else {
            telegramService.sendMessagesToAdmins(message);
        }
    }

    /**
     * Saves the given GuteNachtCustomize value for the user as author
     * if the user is not the author of the GuteNachtCustomize, an error message is sent to the user
     */
    public void saveCustomize(String name, String value, TelegramUser user) {
        try {
            guteNachtCustomizeComponent.saveCustomize(name, value, user);
            telegramService.sendMessage("Customize %s wurde gespeichert".formatted(name), user);
        } catch (UserNotAllowedException e) {
            telegramService.sendMessage(e.getMessage(), user);
        }
    }

    /**
     * Removes the given GuteNachtCustomize for the user as author
     * if the user is not the author of the GuteNachtCustomize, an error message is sent to the user
     */
    public void removeCustomize(String name, TelegramUser user) {
        try {
            boolean isDeleted = guteNachtCustomizeComponent.removeCustomize(name, user);
            if(isDeleted) {
                telegramService.sendMessage("Customize %s wurde gelöscht".formatted(name), user);
            } else {
                telegramService.sendMessage("Customize %s konnte nicht gelöscht werden".formatted(name), user);
            }
        } catch (UserNotAllowedException e) {
            telegramService.sendMessage(e.getMessage(), user);
        }
    }

    /**
     * Sends the GuteNachtCustomize Infos to the user
     */
    public void sendCustomizeInfos(String customizeName, TelegramUser user) {
        guteNachtCustomizeComponent.getCustomize(customizeName).ifPresentOrElse(
                customize -> telegramService.sendMessage("Customize %s - Author:%s%n%s".formatted(customizeName,customize.getAuthor(), customize.getPrompt()), user),
                () -> telegramService.sendMessage("Customize %s konnte nicht gefunden werden".formatted(customizeName), user));
    }

    /**
     * Lists all GuteNachtCustomizes and sends them to the user
     */
    public void sendListCustomizesToUser(TelegramUser user) {
        StringBuilder message = new StringBuilder();
        message.append("Aktuelles Customize:%s:%n".formatted(getCurrentCustomize(user).getName()));
        message.append("Verfügbare Customizes:\n");
        guteNachtCustomizeComponent.getAllCustomizes().forEach(customize -> message.append("/customize_set_%s%n".formatted(customize.getName())));
        telegramService.sendMessage(message.toString(), user);
    }

    /**
     * Returns the current GuteNachtCustomize for the user
     */
    public GuteNachtCustomize getCurrentCustomize(TelegramUser user) {
        return guteNachtCustomizeComponent.getCurrentCustomize(user);
    }

    /**
     * Sends Infos of the current GuteNachtCustomize to the user
     */
    public void sendCurrentCustomizeToUser(TelegramUser user) {
        GuteNachtCustomize customize = getCurrentCustomize(user);
        telegramService.sendMessage("Aktuelles Customize:%s".formatted(customize.getName()), user);
    }

    public void setCustomizeToUser(String customizeName, TelegramUser user) {
        try {
            GuteNachtCustomize guteNachtCustomize = guteNachtCustomizeComponent.setCustomize(customizeName, user);
            telegramService.sendMessage("Customize %s wurde gesetzt".formatted(guteNachtCustomize.getName()), user);
        } catch (NotFoundException e) {
            telegramService.sendMessage("Customize %s konnte nicht gefunden werden".formatted(customizeName), user);
        }
    }

    /**
     * Saves the given GuteNachtCustomize value for the user
     * @param customize
     * @param user
     */
    public void updateGuteNachtCustomize(GuteNachtCustomize customize, TelegramUser user) {
        guteNachtConfigComponent.updateGuteNachtCustomize(customize, user);
    }

}
