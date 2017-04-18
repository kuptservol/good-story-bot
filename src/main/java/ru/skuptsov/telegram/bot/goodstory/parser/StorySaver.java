package ru.skuptsov.telegram.bot.goodstory.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Genre;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Language;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Length;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Character.isWhitespace;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.Type.PART;

@Service
public class StorySaver {
    private final static Logger log = LoggerFactory.getLogger(StorySaver.class);

    private final static int PART_LENGTH_START =
            Length.TEN_MINUTE.getWordsNumberRange().upperEndpoint();
    private static final String FILE_NAME_SEPARATOR = "@";

    @Autowired
    private StoryRepository storyRepository;

    public void processFile(Path path) {
        log.debug("Start processing file {}", path);

        String[] filePatternArray = path.getFileName().toString().split(FILE_NAME_SEPARATOR);
        if (filePatternArray.length != 3) {
            log.error("Skipping incorrect fileName pattern {}", path.getFileName().toString());
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
            save(reader, filePatternArray[0], filePatternArray[1], filePatternArray[2].substring(0, 4));
        } catch (Exception ex) {
            log.error("Skipping incorrect file {}", path, ex);
            return;
        }

        log.debug("Finished processing file {}", path);
    }

    private void save(BufferedReader fileReader, String name, String author, String year) throws IOException {
        log.debug("Saving new book {}, {}, {} started", author, name, year);

        int wordCount = 0;
        StringBuilder text = new StringBuilder();
        int nextChar;
        while ((nextChar = fileReader.read()) != -1 && wordCount < PART_LENGTH_START) {
            if (isWhitespace(nextChar)) {
                wordCount++;
            }
            text.append((char) nextChar);
        }

        if (wordCount < PART_LENGTH_START) {
            save(text.toString(), year, author, name, Type.STORY, 1, wordCount);
        } else {
            save(text.toString(), year, author, name, PART, 1, wordCount);
            saveRestFile(fileReader, year, author, name);
        }

        log.debug("Saving new book {}, {}, {} finished", author, name, year);
    }

    private void saveRestFile(BufferedReader fileReader, String year, String author, String name) throws IOException {
        int part = 1;

        int wordCount = 0;
        StringBuilder text = new StringBuilder();

        int nextChar;
        while ((nextChar = fileReader.read()) != -1) {
            if (isWhitespace(nextChar)) {
                wordCount++;
            }

            text.append((char) nextChar);
            if (wordCount == PART_LENGTH_START) {
                save(text.toString(), year, author, name, PART, ++part, wordCount);
                text = new StringBuilder();
                wordCount = 0;
            }
        }
    }

    private void save(String text, String year, String author, String name, Type type, Integer part, Integer wordCount) {
        text = normalizeText(text);
        Length textLength = getTextLength(wordCount);

        if (textLength == null) {
            log.warn("Story {} {} {} {} length is not suited, ignoring", text, author, name, type);
            return;
        }

        persist(Story.builder()
                .text(text)
                .author(author)
                .name(name)
                .type(type)
                .language(Language.RUSSIAN)
                .length(textLength)
                .genre(Genre.ANY)
                .part(part)
                .year(Integer.valueOf(year.trim()))
                .build());
    }

    private String normalizeText(String text) {
        return text.replaceAll("<br>", "\n").replaceAll("\\<[^>]*>", "");
    }

    private void persist(Story story) {
        log.debug("Persisting new story {}", story);

        storyRepository.add(story);
    }

    private Length getTextLength(Integer wordNum) {
        return Length.BACK.getLengthByTextLength(wordNum);
    }
}
