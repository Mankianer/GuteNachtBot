package de.mankianer.gutenachtbot.openai.models;

import lombok.Data;

@Data
public class Choice {
    private int index;
    private Message message;
    private String finishReason;
}
