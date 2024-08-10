package de.mankianer.gutenachtbot.openai.models;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String id;
    private String object;
    private String model;
    private Long created;
    private List<Choice> choices;
    private Usage usage;
    private String systemFingerprint;
}
