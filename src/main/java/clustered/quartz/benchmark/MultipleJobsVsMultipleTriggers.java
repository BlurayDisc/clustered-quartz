package clustered.quartz.benchmark;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clustered.quartz.jobs.HelloJob;
import clustered.quartz.util.Util;

public class MultipleJobsVsMultipleTriggers {

    private static final Logger log = LoggerFactory.getLogger(MultipleJobsVsMultipleTriggers.class);

    public static void main(String[] args) {

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            final Date futrureDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
            for (int i = 0; i < 100; i++) {
                // define the job and tie it to our HelloJob class
                JobDetail job = JobBuilder.newJob(HelloJob.class)
                        .withIdentity(Util.randomJobName())
                        .storeDurably()
                        .build();

                // Trigger the job to run now, and then repeat every 40 seconds
                Trigger trigger = TriggerBuilder.newTrigger()
                        .startAt(futrureDate)
                        .build();

                // Tell quartz to schedule the job using our trigger
                scheduler.scheduleJob(job, trigger);
            }

            Thread.sleep(Long.MAX_VALUE);

            scheduler.shutdown();

        } catch (Exception se) {
            log.error("Failed to schedule job for execution: ", se);
        }
    }
}
