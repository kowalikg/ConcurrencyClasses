package ao;

import ao.Future.Future;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable{
    private int id;
    private Proxy proxy;
    private Random random = new Random();

    public Consumer(int id, Proxy proxy){
        this.id = id;
        this.proxy = proxy;
    }
    //dodatkowe zadania do wykonania
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
        while(true){
            amount = random.nextInt(proxy.getBufforCapacity()/2);
            //zlecenie zadania
            future = proxy.get(amount);
            //realizacja okreslonej ilosci zadan
            for (int i = 0; i < Values.jobs; i++) extraJob();
            //czekanie na zakonczenie glownego zadania o ile sie nie skonczylo
            future.waitForComplete();
        }
    }
}
