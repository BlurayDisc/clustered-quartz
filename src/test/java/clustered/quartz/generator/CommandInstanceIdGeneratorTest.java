package clustered.quartz.generator;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandInstanceIdGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(CommandInstanceIdGeneratorTest.class);

    public static void main(String[] args) throws SchedulerException {

        try {
            CommandInstanceIdGenerator fail = new CommandInstanceIdGenerator("asd");
            fail.generateInstanceId();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage(), e);
        }

        CommandInstanceIdGenerator succeed1 = new CommandInstanceIdGenerator("hostname");
        log.info("Success: {}", succeed1.generateInstanceId());

        CommandInstanceIdGenerator succeed2 = new CommandInstanceIdGenerator("ls -al");
        log.info("Success: {}", succeed2.generateInstanceId());
    }
}
