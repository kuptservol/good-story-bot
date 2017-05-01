package ru.skuptsov.telegram.bot.goodstory.content.parser;

import com.google.common.primitives.Ints;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.skuptsov.telegram.bot.goodstory.content.StorySaver;
import ru.skuptsov.telegram.bot.goodstory.model.Story;

import java.io.IOException;
import java.io.InputStream;

public abstract class BaseParser {
    private final static Logger log = LoggerFactory.getLogger(BaseParser.class);

    @Autowired
    private StorySaver storySaver;

    @Autowired
    private OkHttpClient http;

    public void parse(String fromUrl) {
        log.debug("Starting to parse from url {}", fromUrl);
        Iterable<String> pagesIterator = getPagesIterator(fromUrl);
        for (String pageUrl : pagesIterator) {
            try {
                sleepGraceFully();
                processPage(pageUrl);
            } catch (Exception e) {
                log.error("Cannot proceed with page {}", pageUrl, e);
            }

        }
    }

    private void sleepGraceFully() throws InterruptedException {
        Thread.sleep(1000);
    }

    private void processPage(String fromUrl) throws IOException {
        log.debug("Processing page parsing from url {}", fromUrl);
        Document doc = getDocument(fromUrl);

        Elements articles = doc.select(getPageArticleSelector());

        for (Element article : articles) {
            String linkToArticle = article.select(getArticleHrefSelector()).attr("href");
            Story storyFromArticle = getStoryFromArticle(linkToArticle);

            storySaver.save(storyFromArticle);
        }

        log.debug("Finshed processing page parsing from url {}", fromUrl);
    }

    private Story getStoryFromArticle(String linkToArticle) throws IOException {
        Document article = getDocument(linkToArticle);

        String text = getAndValidate(article.select(getArticleTextSelector()).text());
        String author = getAndValidate(article.select(getArticleAuthorSelector()).text());
        String name = getAndValidate(article.select(getArticleNameSelector()).text());

        Integer year = Ints.tryParse(article.select(getArticleYearSelector()).text());

        return Story.builder()
                .text(text)
                .author(author)
                .name(name)
                .year(year)
                .build();
    }

    protected abstract String getArticleYearSelector();

    protected abstract String getArticleNameSelector();

    protected abstract String getArticleAuthorSelector();

    protected abstract String getArticleTextSelector();

    protected abstract String getArticleHrefSelector();

    protected abstract String getPageArticleSelector();

    protected abstract Iterable<String> getPagesIterator(String fromUrl);

    private Document getDocument(String linkToArticle) throws IOException {
        return Jsoup.parse(getPageStream(linkToArticle), "UTF-8", linkToArticle);
    }

    private String getAndValidate(String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Text cannot be empty {}");
        }

        return text;
    }

    private InputStream getPageStream(String fromUrl) throws IOException {
        Request request = new Request.Builder()
                .url(fromUrl)
                .build();

        return http.newCall(request).execute().body().byteStream();
    }
}
