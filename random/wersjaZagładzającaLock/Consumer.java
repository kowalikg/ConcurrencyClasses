package com.company;

import java.util.Random;

public class Consumer implements Runnable {
    private int id;
    private int counter;
    private Buffor buffor;
    private Random random = new Random();

    public Consumer(int id, Buffor buffor) {
        this.id = id;
        this.buffor = buffor;
    }

    @Override
    public void run() {
        while (true) {
            buffor.consume(this, random.nextInt(buffor.getCapacity() / 4));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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