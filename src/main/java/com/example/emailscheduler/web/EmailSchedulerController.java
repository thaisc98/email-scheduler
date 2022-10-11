package com.example.emailscheduler.web;

import com.example.emailscheduler.payload.EmailRequest;
import com.example.emailscheduler.quartz.job.EmailJob;
import org.quartz.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class EmailSchedulerController {



    private JobDetail buildJobDetail(EmailRequest schedulerEmailRequest){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", schedulerEmailRequest.getEmail());
        jobDataMap.put("subject", schedulerEmailRequest.getSubject());
        jobDataMap.put("body", schedulerEmailRequest.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-trigger")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
