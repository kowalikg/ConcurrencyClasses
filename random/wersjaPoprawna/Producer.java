package los;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
    private int id;
    private int counter;
    private Buffor buffor;
    private Random random = new Random();

    public Producer(int id, Buffor buffor){
        this.id = id;
        this.buffor = buffor;
    }
    @Override
    public void run() {
        int amount;
        while(true){
            amount = random.nextInt(buffor.getCapacity()/2) + 1;
           // System.out.println("Producer " + id + " begins production. Puts " + amount + " portions.");
            Values.portionsLaunched.addAndGet(amount);
            buffor.produce(this, amount);
           // System.out.println("Producer " + id + " ends production.");

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
