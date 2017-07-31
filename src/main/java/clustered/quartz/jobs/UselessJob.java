package clustered.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UselessJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(UselessJob.class);

    private long processTime;

    private String name;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            log.info("[{}] [RUNNING] [Instance: {}] [Job: {}] [Trigger: {}]",
                    name,
                    context.getJobDetail().getKey(),
                    context.getScheduler().getSchedulerInstanceId(),
                    context.getTrigger().getKey());

            Thread.sleep(processTime);

            log.info("[{}] [COMPLETED] [Instance: {}] [Job: {}] [Trigger: {}]",
                    name,
                    context.getJobDetail().getKey(),
                    context.getScheduler().getSchedulerInstanceId(),
                    context.getTrigger().getKey());

        } catch (Exception e) {
        }
    }

    public void setProcessTime(long processTime) {

        this.processTime = processTime;
    }

    public void setName(String name) {

        this.name = name;
    }

}
