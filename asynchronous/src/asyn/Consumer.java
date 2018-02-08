package asyn;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private int id;
    private int counter;
    private Monitor monitor;
    private Random random = new Random();
    private Buffor buffor;

    public Consumer(int id, Monitor monitor, Buffor buffor){
        this.id = id;
        this.monitor = monitor;
        this.buffor = buffor;
    }

    @Override
    public void run() {
        ArrayList<Integer> positionList;
        int amount;
        while(true){
            amount = random.nextInt(buffor.getCapacity()/2);
            //System.out.println("Consumer " + id + " begins consumption");
            positionList = monitor.consumeBegin(amount);
            buffor.take(this, positionList);
            monitor.consumeEnd(positionList);

            for (int i = 0; i < Values.jobs; i++) extraJob();
           // System.out.println("Consumer " + id + " ends consumption");

        }
    }

    private void extraJob() {
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Values.jobsDone.addAndGet(1);
    }
    public int getId() {
        return id;
    }

    public int getCounter() {
        return counter;
    }
    public void incrementCounter() {
        counter++;
    }
}
