package los;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffor {
    private int capacity;
    private int occupiedSpaces;

    private ReentrantLock lock = new ReentrantLock();

    private Condition firstProducer = lock.newCondition();
    private Condition restProducers = lock.newCondition();
    private Condition firstConsumer = lock.newCondition();
    private Condition restConsumers = lock.newCondition();

    private boolean firstProducerEmpty = true;
    private boolean firstConsumerEmpty = true;
    private int currentProducerWaitingId;
    private int currentConsumerWaitingId;


    public Buffor(int capacity){
       this.capacity = capacity;
    }
    public void produce(Producer p, int amount){
        lock.lock();
        if(!firstProducerEmpty){
            try {
                //System.out.print("Producer " + p.getId() + " waits for " + p.getCounter() + " time, ");
                p.incrementCounter();
                //System.out.println("because producer " + currentProducerWaitingId + " waits for space.");
                restProducers.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        firstProducerEmpty = false;
        currentProducerWaitingId = p.getId();
        while(capacity - occupiedSpaces < amount) {
           // System.out.println("Producer " + p.getId() + " waits for space. To give: " + amount +
          //          ", free space: " + (capacity - occupiedSpaces));
            try {
                firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       // System.out.println("Producer " + p.getId() + " finally is giving " + amount + " pieces."); // do pokazania ze dziala
        occupiedSpaces += amount;
        Values.portionsProduced.addAndGet(amount);

        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!lock.hasWaiters(firstProducer)) {
            restProducers.signal();
        }
        if (!lock.hasWaiters(restProducers)) {
            firstProducerEmpty = true;
        }

        firstConsumer.signal();
        lock.unlock();

    }

    public void consume(Consumer c, int amount){
        lock.lock();
        if (!firstConsumerEmpty){
            try {
              //  System.out.print("Consumer " + c.getId() + " waits for " + c.getCounter() + " time, ");
                c.incrementCounter();
              //  System.out.println("because consumer " + currentConsumerWaitingId + " waits for products.");
                restConsumers.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        firstConsumerEmpty = false;
        currentConsumerWaitingId = c.getId();
        while(occupiedSpaces <= 0) {
            try {
                firstConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("Consumer " + c.getId() + " finally is taking " + amount + " pieces.");
        occupiedSpaces -= amount;
        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!lock.hasWaiters(firstConsumer)) {
            restConsumers.signal();
        }
        firstConsumerEmpty = true;
        firstProducer.signal();
        lock.unlock();
    }

    public int getCapacity() {
        return capacity;
    }
}
