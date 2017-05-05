package ru.skuptsov.telegram.bot.goodstory;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CronTask implements Job {

    public CronTask() {
        System.out.println("Constructor called");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("cron");
    }
}
