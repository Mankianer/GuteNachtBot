package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.core.models.GuteNachtCustomize;
import de.mankianer.gutenachtbot.openai.OpenAIAPIService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class GuteNachtGeschichteComponent {

    public static final String GUTENACHTGESCHICHTEN_PROMPT_TEMPLATE = """
            Gib mir eine kurze Gutenachtgeschichte mit folgendem Inhalt:
            %s
            
            Und schreibe sie im Stil:
            %s
            """;
    private final OpenAIAPIService openAIAPIService;
    @Setter
    private GuteNachtService guteNachtService;

    public GuteNachtGeschichteComponent(OpenAIAPIService openAIAPIService) {
        this.openAIAPIService = openAIAPIService;
    }

    @Transactional
    public Optional<String> getGuteNachtGeschichte(TelegramUser user) {
        return guteNachtService.getCurrentGuteNachtInhalt().map(inhalt -> {
            GuteNachtCustomize customize = guteNachtService.getCurrentCustomize(user);
            return GUTENACHTGESCHICHTEN_PROMPT_TEMPLATE.formatted(inhalt.getInhalt(), customize.getPrompt());
        }).flatMap(openAIAPIService::completeText);
    }
}
