package com.company;

import java.util.Random;

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
            amount = id == 1 ? (buffor.getCapacity() / 2) : random.nextInt(buffor.getCapacity() / 4) ;
            buffor.produce(this, amount);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
