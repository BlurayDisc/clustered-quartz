package clustered.quartz.benchmark;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clustered.quartz.jobs.UselessJob;
import clustered.quartz.util.Util;

/**
 * <pre>
 * Steps to test:
 * 
 * 1. Configure a long running job so sub-tasks can be performed on the other instance.
 * 2. Start instance-A and schedule a long running job. 
 * 3. Start instance-B with the scheduler on standby mode.
 * 4. Shutdown instance-A and wait for instance-B to detect the failed instance.
 * 5. Confirm instance-B recovers the failed job from instance-A with a different trigger id.
 * 
 * </pre>
 * 
 * @author RuN
 *
 */
public class ClusterFailover {

    private static final Logger log = LoggerFactory.getLogger(ClusterFailover.class);

    private static Scheduler scheduler;

    public static void main(String[] args) {

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            final JobDetail job = JobBuilder.newJob(UselessJob.class)
                    .withIdentity(Util.randomJobName())
                    .requestRecovery()
                    .build();

            final Instant futureDate = Instant.now().plus(15, ChronoUnit.SECONDS);

            final Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(Date.from(futureDate))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .usingJobData("name", "Job")
                    .usingJobData("processTime", 10000L)
                    .build();

            scheduler.scheduleJob(job, trigger);

            Thread.sleep(Long.MAX_VALUE);

            scheduler.shutdown();

        } catch (Exception e) {
            log.error("Failed to schedule job for execution: ", e);
        }
    }
}
