package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skuptsov.telegram.bot.goodstory.model.Subscription;
import ru.skuptsov.telegram.bot.goodstory.repository.SubscribeRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class SubscribeRepositoryImpl implements SubscribeRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void save(Subscription subscription) {
        getSession()
                .save(subscription);
    }

    @Override
    @Transactional
    public void delete(Subscription subscription) {
        getSession()
                .delete(subscription);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subscription> get(long chatId) {
        return ofNullable((Subscription) getSession().byId(Subscription.class).load(chatId));
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Subscription> getAll() {
        return getSession().createCriteria(Subscription.class).list();
    }
}
