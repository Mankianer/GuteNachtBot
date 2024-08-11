package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.GuteNachtService;
import de.mankianer.gutenachtbot.openai.OpenAIAPIService;
import de.mankianer.gutenachtbot.telegram.models.TelegramUser;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GuteNachtGeschichteComponent {

    private final OpenAIAPIService openAIAPIService;
    private String prompt = "Gib mir eine kurze Gutenachtgeschichte";
    @Setter
    private GuteNachtService guteNachtService;

    public GuteNachtGeschichteComponent(OpenAIAPIService openAIAPIService) {
        this.openAIAPIService = openAIAPIService;
    }

    public Optional<String> getGuteNachtGeschichte(TelegramUser user) {
        //TODO erweitern auf Inhalt + Customize
        return  this.openAIAPIService.completeText(prompt);
    }
}
