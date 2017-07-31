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
 * 1. Configure several short running jobs to run repeatedly to simulate busy schedulers.
 * 2. Start multiple instances i.e. instance-A, instance-B, instance-C...
 * 3. Schedule jobs only with instance-A and having the rest of the instances on standby.
 * 4. Confirm load balancing mechanism had been applied across multiple instances.
 * 
 * </pre>
 * 
 * @author RuN
 *
 */
public class ClusterLoadBalancing {

    private static final Logger log = LoggerFactory.getLogger(ClusterLoadBalancing.class);

    private static Scheduler scheduler;

    public static void main(String[] args) {

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            for (int i = 1; i < 4; i++) {
                final JobDetail job = JobBuilder.newJob(UselessJob.class)
                        .withIdentity(Util.randomJobName())
                        .build();

                final Instant futureDate = Instant.now().plus(15, ChronoUnit.SECONDS);

                final Trigger trigger = TriggerBuilder.newTrigger()
                        .startAt(Date.from(futureDate))
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever())
                        .usingJobData("name", "Job-" + i)
                        .usingJobData("processTime", 100L)
                        .build();

                scheduler.scheduleJob(job, trigger);
            }

            Thread.sleep(Long.MAX_VALUE);

            scheduler.shutdown();

        } catch (Exception e) {
            log.error("Failed to schedule job for execution: ", e);
        }
    }
}
