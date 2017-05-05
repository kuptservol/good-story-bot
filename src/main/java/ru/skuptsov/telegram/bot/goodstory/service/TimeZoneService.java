package ru.skuptsov.telegram.bot.goodstory.service;

import org.joda.time.DateTimeZone;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.Location;

public interface TimeZoneService {
    DateTimeZone getDefaultTimeZone();

    DateTimeZone getTimeZone(Location location);

    DateTimeZone getServerTimeZone();
}
