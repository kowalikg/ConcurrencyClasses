package ao;

import ao.Future.Future;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable{
    private int id;
    private Proxy proxy;
    private Random random = new Random();

    public Producer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
    }
    private void extraJob() {
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Values.jobsDone.addAndGet(1);
    }
    @Override
    public void run() {
        int amount;
        Future future;
        while (true){
            // analogicznie jak w konsumencie
            amount = random.nextInt(proxy.getBufforCapacity()/2);
            future = proxy.put(amount);
            Values.portionsLaunched.addAndGet(amount);
            for (int i = 0; i < Values.jobs; i++) extraJob();
            future.waitForComplete();

        }

    }
}
