package de.mankianer.gutenachtbot.openai;

import de.mankianer.gutenachtbot.openai.models.ChatRequest;
import de.mankianer.gutenachtbot.openai.models.ChatResponse;
import de.mankianer.gutenachtbot.openai.models.Message;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class ChatCompletionComponent {

    private final RestTemplate openaiRestTemplate;

    @Value("${openai.chat_completion.url}")
    private String apiUrl;
    @Value("${openai.model:gpt-4o-mini}")
    private String model;
    @Value("${openai.temperature:0.7}")
    private double temperature;

    public ChatCompletionComponent(RestTemplate openaiRestTemplate) {
        this.openaiRestTemplate = openaiRestTemplate;
    }

    /**
     * Completes the text of a prompt with a simple chat
     * @param prompt
     * @return Optional of the completed text if successful, empty otherwise
     */
    public Optional<String> completeText(String prompt) {
        Message message = new Message("user", prompt);
        ChatRequest chatRequest = new ChatRequest(model, List.of(message), temperature);
        ChatResponse chatResponse = completeChat(chatRequest).orElse(null);
        if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(chatResponse.getChoices().getFirst().getMessage().getContent());
    }

    /**
     * Calls the OpenAI API to complete a chat
     * @param chatRequest
     * @return Optional of ChatResponse if successful, empty otherwise
     */
    public Optional<ChatResponse> completeChat(ChatRequest chatRequest) {
        try {
            ChatResponse chatResponse = openaiRestTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            logUsageChatResponse(chatResponse);
            return Optional.ofNullable(chatResponse);
        } catch (RestClientException e) {
            log.error("Error while calling OpenAI API", e);
            return Optional.empty();
        }
    }

    private void logUsageChatResponse(ChatResponse chatResponse) {
        if(chatResponse != null && chatResponse.getUsage() != null) {
            log.info("OpenAI API usage: {}", chatResponse.getUsage());
        }
    }
}
