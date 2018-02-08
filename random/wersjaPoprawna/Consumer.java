package los;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private int id;
    private int counter;
    private Buffor buffor;
    private Random random = new Random();

    public Consumer(int id, Buffor buffor){
        this.id = id;
        this.buffor = buffor;
    }
    @Override
    public void run() {
        int amount;
        while(true){
            amount = random.nextInt(buffor.getCapacity() / 2) + 1;
          //  System.out.println("Consumer " + id + " begins consumption");
            buffor.consume(this, amount);
         //   System.out.println("Consumer " + id + " ends consumption. Takes " + amount + " portions.");

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
