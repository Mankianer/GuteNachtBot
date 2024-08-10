package de.mankianer.gutenachtbot.core.components;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
@Component
public class FileComponent {

    @Value("${gutenachtbot.basePath}")
    private Path basePath;

    /**
     * Read the GuteNachtInhalt for a specific date from the file system
     * Path: {basePath}/inhalt/yyyy-MM-dd.txt
     * @param date
     * @return
     */
    public Optional<String> readGuteNachtInhalt(LocalDate date) {
        Path guteNachtInhaltPath = getGuteNachtInhaltPath(date);
        return Optional.ofNullable(readFile(guteNachtInhaltPath));
    }

    /**
     * Save the GuteNachtInhalt for a specific date to the file system
     * Path: {basePath}/inhalt/yyyy-MM-dd.txt
     * @param date
     * @param content
     */
    public void saveGuteNachtInhalt(LocalDate date, String content) {
        Path guteNachtInhaltPath = getGuteNachtInhaltPath(date);
        saveFile(guteNachtInhaltPath, content);
    }

    private Path getGuteNachtInhaltPath(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return basePath.resolve("inhalt").resolve(formattedDate + ".txt");
    }

    private void saveFile(Path path, String content) {
        try {
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            log.error("Error while saving file", e);
        }
    }

    private String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }
}
