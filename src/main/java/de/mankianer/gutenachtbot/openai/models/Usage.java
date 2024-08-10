package de.mankianer.gutenachtbot.openai.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Usage {
    @JsonAlias("prompt_tokens")
    private Integer promptTokens;
    @JsonAlias("completion_tokens")
    private Integer completionTokens;
    @JsonAlias("total_tokens")
    private Integer totalTokens;
}
