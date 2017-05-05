package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.StorySeen;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Genre;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Repository
public class StoryRepositoryImpl implements StoryRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public Story getStoryUnseen(@NotNull StoryQuery storyQuery, long chatId) {
        Query query = sessionFactory.getCurrentSession()
                .createSQLQuery("" +
                        "SELECT * FROM story s " +
                        "WHERE NOT EXISTS " +
                        "(SELECT chat_id FROM story_seen ss WHERE chat_id = :userId AND ss.story_id = s.id) " +
                        (storyQuery.getType() != null ? "AND type = :type " : "") +
                        (storyQuery.getLength() != null ? "AND length = :length " : "") +
                        (storyQuery.getGenre() != null ? storyQuery.getGenre() != Genre.ANY ? "AND genre = :genre " : "" : "") +
                        (storyQuery.getLanguage() != null ? "AND language = :language " : "") +
                        "ORDER BY RANDOM() " +
                        "LIMIT 1")
                .addEntity(Story.class)
                .setParameter("userId", chatId);


        if (storyQuery.getType() != null) {
            query.setParameter("type", storyQuery.getType().toString());
        }

        if (storyQuery.getLanguage() != null) {
            query.setParameter("language", storyQuery.getLanguage().toString());
        }

        if (storyQuery.getGenre() != null && storyQuery.getGenre() != Genre.ANY) {
            query.setParameter("genre", storyQuery.getGenre().toString());
        }

        if (storyQuery.getLength() != null) {
            query.setParameter("length", storyQuery.getLength().toString());
        }

        return (Story) query.uniqueResult();
    }

    @Override
    @Transactional
    public void markStoryAsSeen(long storyId, long chatId) {
        sessionFactory.getCurrentSession()
                .save(StorySeen.builder()
                        .storyId(storyId)
                        .chatId(chatId)
                        .build());
    }

    @Override
    @Transactional
    public void add(Story story) {
        sessionFactory.getCurrentSession().save(story);
    }
}
