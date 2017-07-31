package clustered.quartz.util;

import org.apache.commons.lang3.RandomStringUtils;

@SuppressWarnings("deprecation")
public class Util {

    public static String randomJobName() {

        return String.format(
                "Job-%s-%s",
                RandomStringUtils.randomAlphabetic(6),
                RandomStringUtils.randomNumeric(4));
    }
}
