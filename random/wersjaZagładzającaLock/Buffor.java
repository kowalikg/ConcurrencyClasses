package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffor {
    private int capacity;
    private int occupiedSpaces;

    private ReentrantLock lock = new ReentrantLock();
    private Condition consumentsCondition = lock.newCondition();
    private Condition producentsCondition = lock.newCondition();

    public Buffor(int capacity){
       this.capacity = capacity;
    }
    public void produce(Producer p, int amount){
        lock.lock();
        while(capacity - occupiedSpaces < amount) {
            System.out.println("Producer " + p.getId() + " waits for " + p.getCounter() + " time.");
            p.incrementCounter();
            try {
                producentsCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Producer " + p.getId() + " finally is giving " + amount + " pieces."); // do pokazania ze dziala
        occupiedSpaces += amount;
        consumentsCondition.signal();
        lock.unlock();

    }

    public void consume(Consumer c, int amount){
        lock.lock();
        while(occupiedSpaces <= 0) {
            try {
                //System.out.print("Consumer " + c.getId() + " waits for " + c.getCounter() + " time.");
                consumentsCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println("Consumer " + c.getId() + " finally is giving " + amount + " pieces.");
        occupiedSpaces -= amount;
        producentsCondition.signal();
        lock.unlock();
    }

    public int getCapacity() {
        return capacity;
    }
}
