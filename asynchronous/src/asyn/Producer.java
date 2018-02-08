package asyn;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
    private int id;
    private int counter;
    private Monitor monitor;
    private Buffor buffor;
    private Random random = new Random();
    public Producer(int id, Monitor monitor, Buffor buffor){
        this.id = id;
        this.monitor = monitor;
        this.buffor = buffor;
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
        ArrayList<Integer> positionList;
        while(true){
            amount = random.nextInt(buffor.getCapacity()/2);
            //System.out.println("Producer " + id + " begins production");
            positionList = monitor.produceBegin(amount);
            Values.portionsLaunched.addAndGet(amount);

            buffor.push(this, positionList);
            monitor.produceEnd(positionList);
            for (int i = 0; i < Values.jobs; i++) extraJob();
          //  System.out.println("Producer " + id + " ends production");
          //  System.out.println("Producer " + id + " ends another job");


        }
    }

    public int getCounter() {
        return counter;
    }
    public int getId() {
        return id;
    }

    public void incrementCounter() {
        counter++;
    }
}
