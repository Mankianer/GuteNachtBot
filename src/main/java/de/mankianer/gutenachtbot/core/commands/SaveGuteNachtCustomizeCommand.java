package de.mankianer.gutenachtbot.core.commands;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.telegram.TelegramService;
import de.mankianer.gutenachtbot.telegram.commands.SimpleCommand;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command to save a GuteNachtCustomize
 * /customize_save <name> "<value>"
 */
@Component
public class SaveGuteNachtCustomizeCommand extends SimpleCommand {

    public static final String COMMAND_REGEX = "/customize_save\\s+(\\w+)\\s+\"([^\"]+)\"";
    private final GuteNachtService guteNachtService;
    private final TelegramService telegramService;
    private final Pattern pattern;

    public SaveGuteNachtCustomizeCommand(GuteNachtService guteNachtService, TelegramService telegramService) {
        super("/customize_save", true);
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
                pair -> guteNachtService.saveCustomize(pair.getFirst(), pair.getSecond(), user),
                () -> telegramService.sendMessage("Fehler beim ein Lesen der Argumente für den Command: " + getCommand(), user));
    }

    public Optional<Pair<String, String>> extractArguments(String input) {
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            return Optional.of(Pair.of(name, value));
        } else {
            return Optional.empty();
        }
    }
}
