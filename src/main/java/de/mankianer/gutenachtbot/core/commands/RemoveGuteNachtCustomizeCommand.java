package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to save a GuteNachtCustomize
 * /customize_remove <name>
 */
@Component
@Order(2)
public class RemoveGuteNachtCustomizeCommand extends SimpleCommand {

    public static final String COMMAND_REGEX = "/customize_remove\\s+(\\w+)";
    private final GuteNachtService guteNachtService;
    private final TelegramService telegramService;
    private final Pattern pattern;

    public RemoveGuteNachtCustomizeCommand(GuteNachtService guteNachtService, TelegramService telegramService) {
        super("/customize_remove","Zum Löschen von Customize-Profilen. (/customize_remove <CUSTOMIZE_NAME>)",  true);
        this.guteNachtService = guteNachtService;
        this.telegramService = telegramService;
        pattern = Pattern.compile(COMMAND_REGEX);
    }

    @Override
    public boolean matchesMessage(String message, TelegramUser user) {
        return pattern.matcher(message).matches();
    }

    @Override
    public void onExecute(Update update, TelegramUser user) {
        String input = update.getMessage().getText();
        extractArguments(input).ifPresentOrElse(
                name -> guteNachtService.removeCustomize(name, user),
                () -> telegramService.sendMessage("Fehler beim ein Lesen der Argumente für den Command: " + getCommand(), user));
    }

    public Optional<String> extractArguments(String input) {
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String name = matcher.group(1);
            return Optional.of(name);
        } else {
            return Optional.empty();
        }
    }
}
