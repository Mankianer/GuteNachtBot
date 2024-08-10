package de.mankianer.gutenachtbot.openai;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OpenAIAPIService {

    private final ChatCompletionComponent chatCompletionComponent;

    public OpenAIAPIService(ChatCompletionComponent chatCompletionComponent) {
        this.chatCompletionComponent = chatCompletionComponent;
    }

    public Optional<String> completeText(String prompt) {
        return chatCompletionComponent.completeText(prompt);
    }
}
