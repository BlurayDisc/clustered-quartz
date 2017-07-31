package clustered.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clustered.quartz.jobs.HelloJob;
import clustered.quartz.util.Util;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static Scheduler scheduler;

    public static void main(String[] args) {

        try {
            // Grab the Scheduler instance from the Factory
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();

            //scheduleJob();

            Thread.sleep(Long.MAX_VALUE);

            scheduler.shutdown();

        } catch (Exception se) {
            log.error("Failed to schedule job for execution: ", se);
        }
    }

    static void scheduleJob() throws SchedulerException {

        // define the job and tie it to our HelloJob class
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity(Util.randomJobName())
                .requestRecovery()
                .build();

        // Trigger the job to run now, and then repeat every 40 seconds
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
    }
}
