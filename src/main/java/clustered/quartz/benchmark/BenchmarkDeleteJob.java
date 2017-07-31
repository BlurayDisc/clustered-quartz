package clustered.quartz.benchmark;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clustered.quartz.jobs.UselessJob;
import clustered.quartz.util.Util;

public class BenchmarkDeleteJob {

    private static final Logger log = LoggerFactory.getLogger(BenchmarkDeleteJob.class);

    private static Scheduler scheduler;

    private static List<JobKey> jobKeys = new ArrayList<JobKey>();

    public static void main(String[] args) {

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            long start = System.nanoTime();

            // Create 100 jobs in the database.
            int i;
            for (i = 1; i < 1000; i++) {
                final JobKey jobKey = JobKey.jobKey(Util.randomJobName());
                final JobDetail job = JobBuilder.newJob(UselessJob.class)
                        .withIdentity(jobKey)
                        .build();

                jobKeys.add(jobKey);

                final Instant futureDate = Instant.now().plus(i, ChronoUnit.HOURS);

                final Trigger trigger = TriggerBuilder.newTrigger()
                        .startAt(Date.from(futureDate))
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                        .usingJobData("name", "Job" + i)
                        .usingJobData("processTime", 10000L)
                        .build();

                scheduler.scheduleJob(job, trigger);
            }

            // Then bulk delete all jobs from the db.
            scheduler.deleteJobs(jobKeys);

            long end = System.nanoTime();

            log.info("Total Time spent to create and delete {} jobs: {}ms", i, TimeUnit.NANOSECONDS.toMillis(end - start));

            scheduler.shutdown();

        } catch (Exception e) {
            log.error("Failed to schedule job for execution: ", e);
        }
    }
}
