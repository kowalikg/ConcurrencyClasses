package ao;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Buffor {
    private int capacity;
    private int occupiedSpaces;

    public Buffor(int capacity){
        this.capacity = capacity;
    }
    public void put(int amount){
        occupiedSpaces += amount;
        Values.portionsProduced.addAndGet(amount);
        //obciążamy bufor
        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean get(int portions){
        occupiedSpaces -= portions;
        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public int getCapacity(){
        return capacity;
    }
    public int getOccupiedSpaces(){
        return occupiedSpaces;
    }
}
