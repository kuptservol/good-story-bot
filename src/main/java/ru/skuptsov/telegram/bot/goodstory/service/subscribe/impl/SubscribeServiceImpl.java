package ru.skuptsov.telegram.bot.goodstory.service.subscribe.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.skuptsov.telegram.bot.goodstory.model.Subscription;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.SubscribeRepository;
import ru.skuptsov.telegram.bot.goodstory.service.TimeZoneService;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.Location;
import ru.skuptsov.telegram.bot.goodstory.service.subscribe.SubscribeService;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.lang.String.format;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    private final Logger log = LoggerFactory.getLogger(SubscribeServiceImpl.class);

    private final static String EVERY_DAY_CRON_FORMAT = "0 0 %d * * ? *";

    @Autowired
    private SubscribeRepository repository;

    @Autowired
    private TimeZoneService timeZoneService;

    @Autowired
    private Scheduler scheduler;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public void add(@NotNull String time, @NotNull StoryQuery storyQuery, long chatId) {
        add(time, storyQuery, chatId, timeZoneService.getDefaultTimeZone());
    }

    @Async
    @Override
    public void add(LocalTime localTime, @Nullable Location location, StoryQuery storyQuery, long chatId) {
        add(localTime.toString(), storyQuery, chatId, timeZoneService.getTimeZone(location));
    }

    @Override
    public Optional<Subscription> get(long chatId) {
        return repository.get(chatId);
    }

    private String getCronExpression(String time, String timeZoneId) {
        LocalTime localTime = new LocalTime(time);

        DateTime serverTriggerTime = localTime.toDateTimeToday(DateTimeZone.forID(timeZoneId))
                .toDateTime(timeZoneService.getServerTimeZone());

        return format(EVERY_DAY_CRON_FORMAT, serverTriggerTime.getHourOfDay());
    }

    @Override
    public void remove(long chatId) {
        removeTaskScheduling(chatId);
        repository.delete(
                Subscription.builder()
                        .chatId(chatId)
                        .build());
    }

    private void add(String time, StoryQuery storyQuery, long chatId, DateTimeZone timeZone) {
        try {
            String query = jsonMapper.writeValueAsString(storyQuery);
            scheduleTask(time, query, chatId, timeZone.toString());
            repository.save(Subscription.builder()
                    .chatId(chatId)
                    .time(time)
                    .timezone(timeZone.toString())
                    .query(query)
                    .build());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot serialize story query : {" + storyQuery + "}", e);
        } catch (SchedulerException e) {
            throw new IllegalArgumentException("Error while scheduling", e);
        }
    }

    private void removeTaskScheduling(long chatId) {
        try {
            scheduler.deleteJob(getJobKey(chatId));
        } catch (SchedulerException e) {
            log.error("Can't delete scheduling fro chatId : {}", chatId);
        }
    }

    private JobKey getJobKey(long chatId) {
        return new JobKey(String.valueOf(chatId));
    }

    private void scheduleTask(String time, String storyQuery, Long chatId, String timeZoneId) throws SchedulerException {
        String cronExpression = getCronExpression(time, timeZoneId);
        log.debug("Scheduling {} {} {} {} {}", time, storyQuery, chatId, timeZoneId, cronExpression);
        JobDetail job = newJob(SendStoryTask.class)
                .usingJobData("storyQuery", storyQuery)
                .usingJobData("chatId", chatId.toString())
                .withIdentity(getJobKey(chatId))
                .build();

        Trigger trigger = newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    @PostConstruct
    public void startSchedulers() {
        log.debug("Start existing schedulers from db");
        for (Subscription subscription : repository.getAll()) {
            try {
                scheduleTask(
                        subscription.getTime(),
                        subscription.getQuery(),
                        subscription.getChatId(),
                        subscription.getTimezone()
                );
            } catch (SchedulerException e) {
                log.error("Error while scheduling", e);
            }
        }

        log.debug("Finished starting existing schedulers from db");
    }
}
