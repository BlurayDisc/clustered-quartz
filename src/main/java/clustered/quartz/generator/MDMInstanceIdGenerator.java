package clustered.quartz.generator;

import org.quartz.spi.InstanceIdGenerator;

/**
 * InstanceIdGenerator that names the scheduler instance using just the CURRENT_TIME.
 */
public class MDMInstanceIdGenerator implements InstanceIdGenerator {

    public String generateInstanceId() {

        return String.format("MDM-%s", System.currentTimeMillis());
    }

}
