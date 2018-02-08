package los;

import java.util.concurrent.atomic.AtomicInteger;

public class Values {
    public static AtomicInteger portionsLaunched = new AtomicInteger(0);
    public static AtomicInteger portionsProduced = new AtomicInteger(0);
    public static int threadDelay;
    public static AtomicInteger jobsDone = new AtomicInteger(0);
    public static int jobs;
    public static int portions;
}
