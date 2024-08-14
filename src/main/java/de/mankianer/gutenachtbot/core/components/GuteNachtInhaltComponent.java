package de.mankianer.gutenachtbot.core.components;

import de.mankianer.gutenachtbot.core.models.GuteNachtInhalt;
import de.mankianer.gutenachtbot.openai.OpenAIAPIService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class GuteNachtInhaltComponent {

    private final OpenAIAPIService openAIAPIService;
    private final FileComponent fileComponent;
    @Value("${gutenacht.inhalt.prompt}")
    private String inhaltPrompt;
    @Value("#{${gutenacht.inhalt.themen}}")
    private List<String> inhaltThemen;

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
        String thema = inhaltThemen.get((int) (Math.random() * inhaltThemen.size()));
        log.info("Create new GuteNachtInhalt with Thema: {}", thema);
        return openAIAPIService.completeText("%s%nThema: %s".formatted(inhaltPrompt, thema)).map(inhalt -> {
            GuteNachtInhalt guteNachtInhalt = new GuteNachtInhalt(inhalt, LocalDate.now());
            fileComponent.saveGuteNachtInhalt(LocalDate.now(), inhalt);
            return guteNachtInhalt;
        });
    }
}
