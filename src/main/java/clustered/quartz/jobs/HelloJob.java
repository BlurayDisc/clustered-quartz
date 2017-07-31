package clustered.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(HelloJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            log.info("[{}] [RUNNING] Job: {} Trigger: {}",
                    context.getScheduler().getSchedulerInstanceId(),
                    context.getJobDetail().getKey(),
                    context.getTrigger().getKey());

            Thread.sleep(5000);

            log.info("[{}] [COMPLETED] Job: {} Trigger: {}",
                    context.getScheduler().getSchedulerInstanceId(),
                    context.getJobDetail().getKey(),
                    context.getTrigger().getKey());

        } catch (Exception e) {
        }
    }

}
