package ru.skuptsov.telegram.bot.goodstory.content.parser;

import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class OstrovokParser extends BaseParser {

    @Override
    protected String getArticleYearSelector() {
        return ".created";
    }

    @Override
    protected String getArticleNameSelector() {
        return ".entry-title";
    }

    @Override
    protected String getArticleAuthorSelector() {
        return ".entry-author";
    }

    @Override
    protected String getArticleTextSelector() {
        return ".entry-content";
    }

    @Override
    protected String getArticleHrefSelector() {
        return ".read-more";
    }

    @Override
    protected String getPageArticleSelector() {
        return "article";
    }

    @Override
    protected Iterable<String> getPagesIterator(String fromUrl) {
        return () -> IntStream.range(1, 116).mapToObj(i -> fromUrl + i).iterator();
    }
}
