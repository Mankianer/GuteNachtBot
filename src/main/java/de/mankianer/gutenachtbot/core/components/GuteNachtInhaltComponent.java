package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.models.GuteNachtInhalt;
import de.mankianer.gutenachtbot.openai.OpenAIAPIService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class GuteNachtInhaltComponent {

    private final OpenAIAPIService openAIAPIService;
    private final FileComponent fileComponent;

    public GuteNachtInhaltComponent(OpenAIAPIService openAIAPIService, FileComponent fileComponent) {
        this.openAIAPIService = openAIAPIService;
        this.fileComponent = fileComponent;
    }

    /**
     * Get the GuteNachtInhalt for today
     * if there is no GuteNachtInhalt for today, create a new one
     * if there is no GuteNachtInhalt for today and OpenAI is not available, return empty
     *
     * @return
     */
    public Optional<GuteNachtInhalt> getGuteNachtInhaltToDay() {
        return fileComponent.readGuteNachtInhalt(LocalDate.now())
                .map(inhalt -> new GuteNachtInhalt(inhalt, LocalDate.now()));
    }

    public Optional<GuteNachtInhalt> createNewGuteNachtInhalt() {
        return openAIAPIService.completeText("Gib nur Inhalt für eine Gutenachtgeschichte in Stichpunkten").map(inhalt -> {
            GuteNachtInhalt guteNachtInhalt = new GuteNachtInhalt(inhalt, LocalDate.now());
            fileComponent.saveGuteNachtInhalt(LocalDate.now(), inhalt);
            return guteNachtInhalt;
        });
    }
}
