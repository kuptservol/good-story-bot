package ru.skuptsov.telegram.bot.goodstory.service.impl;

import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;
import ru.skuptsov.telegram.bot.goodstory.service.TimeZoneService;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.Location;

import javax.annotation.PostConstruct;

@Service
public class TimeZoneServiceImpl implements TimeZoneService {

    private static final DateTimeZone DEFAULT_TIME_ZONE = DateTimeZone.forID("Europe/Moscow");

    @Autowired
    private GoodStoryBotConfiguration configuration;

    private DateTimeZone serverTimeZone;

    @Override
    public DateTimeZone getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    @Override
    public DateTimeZone getTimeZone(Location location) {
        // default
        return DEFAULT_TIME_ZONE;
    }

    @Override
    public DateTimeZone getServerTimeZone() {
        return serverTimeZone;
    }

    @PostConstruct
    public void init() {
        serverTimeZone = DateTimeZone.forID(configuration.serverTimeZone);
    }
}
