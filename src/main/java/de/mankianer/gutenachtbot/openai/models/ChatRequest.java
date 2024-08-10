package de.mankianer.gutenachtbot.openai.models;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ChatRequest {
    @NonNull
    private String model;
    @NonNull
    private List<Message> messages;
    @NonNull
    private double temperature;
}
