package com.company;

public class Buffor {
    private int capacity;
    private int occupiedSpaces;

    public Buffor(int capacity){
       this.capacity = capacity;
    }
    public synchronized void produce(Producer p, int amount){
        while(capacity - occupiedSpaces < amount) {
            System.out.println("Producer " + p.getId() + " waits for " + p.getCounter());
            p.incrementCounter();
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (p.getId() == 1) System.out.println("Producer " + p.getId() + " finally is giving " + amount + " pieces."); // do pokazania ze dziala
        occupiedSpaces += amount;
        notifyAll();//notify();

    }

    public synchronized void consume(Consumer c, int amount){
        while(occupiedSpaces <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

       // System.out.println("Consumer " + c.getId() + " finally is taking " + amount + " pieces.");
        occupiedSpaces -= amount;
        notifyAll();//notify();
    }

    public int getCapacity() {
        return capacity;
    }
}
