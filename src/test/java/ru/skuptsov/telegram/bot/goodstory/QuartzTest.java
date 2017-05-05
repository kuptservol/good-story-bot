package ru.skuptsov.telegram.bot.goodstory;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.testng.annotations.Test;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTest {

    @Test
    public void test() throws SchedulerException {

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        JobDetail job = newJob(CronTask.class)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("DailySendStoryTaskTrigger", "Group")
                .startNow()
//                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ? *"))
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(5).repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);

        while (true) {

        }
    }
}
