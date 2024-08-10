package de.mankianer.gutenachtbot.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuteNachtInhalt {

    private String inhalt;
    private LocalDate createdAt;
}
