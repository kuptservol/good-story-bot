package ru.skuptsov.telegram.bot.goodstory.processor.story;

import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Type;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static ru.skuptsov.telegram.bot.goodstory.model.dialog.Type.PART;

@Service
public class StoryTextBuilder {

    public String build(Optional<Story> story) {
        return story
                .map(this::getText)
                .orElse("К сожалению, в этом разделе ничего нового для вас пока нет");
    }

    private String getText(Story story) {
        StringBuilder builder = new StringBuilder();

        if (story.getType() == Type.STORY) {
            builder.append(getName(story))
                    .append(getLineSeparator())
                    .append(getLineSeparator());
        }

        if (story.getType() == PART) {
            builder.append("...");
        }

        builder
                .append(story.getText());

        if (story.getType() == PART) {
            builder.append("...");
        }

        builder.append(getLineSeparator())
                .append(getLineSeparator())
                .append(getAuthor(story))
                .append(" ");

        if (story.getType() == PART) {
            builder
                    .append(getName(story))
                    .append(" ");
        }

        builder
                .append(getYear(story));

        return builder.toString();
    }

    private String getLineSeparator() {
        return "\n";
    }

    private String getName(Story story) {
        return story.getName();
    }

    private String getYear(Story story) {
        return ofNullable(story.getYear())
                .map(year -> year + "г.")
                .orElse("");
    }

    private String getAuthor(Story story) {
        return ofNullable(story.getAuthor()).orElse("");
    }
}
