package clustered.quartz.generator;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandInstanceIdGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(CommandInstanceIdGeneratorTest.class);

    public static void main(String[] args) throws SchedulerException {

        try {
            CommandInstanceIdGenerator fail = new CommandInstanceIdGenerator();
            fail.setCmd("asd");
            fail.generateInstanceId();
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage(), e);
        }

        CommandInstanceIdGenerator succeed1 = new CommandInstanceIdGenerator();
        succeed1.setCmd("hostname");
        log.info("Success: {}", succeed1.generateInstanceId());

        CommandInstanceIdGenerator succeed2 = new CommandInstanceIdGenerator();
        succeed2.setCmd("ls -al");
        log.info("Success: {}", succeed2.generateInstanceId());
    }
}
