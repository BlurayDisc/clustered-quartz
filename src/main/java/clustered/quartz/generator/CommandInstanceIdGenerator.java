package clustered.quartz.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

/**
 * Instance ID generator that names the scheduler instance using the feedback of executing a shell commands from within JVM.
 */
public class CommandInstanceIdGenerator implements InstanceIdGenerator {

    private final String cmd;

    public CommandInstanceIdGenerator(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String generateInstanceId() throws SchedulerException {

        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            return new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .collect(Collectors.joining(" "));
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to generate instance id from command: %s", cmd), e);
        }
    }
}
