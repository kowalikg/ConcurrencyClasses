package asyn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int capacity;

    private ReentrantLock emptyLock = new ReentrantLock();
    private ReentrantLock fullLock = new ReentrantLock();

    private Queue<Integer> empty = new LinkedList<>();
    private Queue<Integer> full = new LinkedList<>();

    private final Condition firstOnEmpty = emptyLock.newCondition();
    private final Condition restOnEmpty = emptyLock.newCondition();
    private final Condition firstOnFull = fullLock.newCondition();
    private final Condition restOnFull = fullLock.newCondition();

    private boolean firstOnEmptyFree = true;
    private boolean firstOnFullFree = true;

    public Monitor(int capacity){
       this.capacity = capacity;
       for (int i = 0 ; i < capacity; i++){
           empty.add(i);
       }
    }
    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Integer> produceBegin(int amount){
        emptyLock.lock();
        ArrayList<Integer> positionList = new ArrayList();

        if(!firstOnEmptyFree){
            try {
                restOnEmpty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        firstOnEmptyFree = false;
        while (empty.size() < amount){
            try{
                firstOnEmpty.await();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for (int i = 0; i < amount; i++){
            positionList.add(empty.poll());
        }

        if (!emptyLock.hasWaiters(firstOnEmpty)) restOnEmpty.signal();
        firstOnEmptyFree = true;
        emptyLock.unlock();
        return positionList;
    }
    public void produceEnd(ArrayList<Integer> positionList){
        fullLock.lock();
        full.addAll(positionList);
        firstOnFull.signal();
        fullLock.unlock();
    }
    public ArrayList<Integer> consumeBegin(int amount){
        fullLock.lock();
        ArrayList<Integer> positionList = new ArrayList();
        if (!firstOnFullFree){
            try {
                restOnFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        firstOnFullFree = false;
        while(full.size() < amount) {
            try {
                firstOnFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < amount; i++){
            positionList.add(full.poll());
        }
        if (!fullLock.hasWaiters(firstOnFull)) restOnFull.signal();
        firstOnFullFree = true;
        fullLock.unlock();
        return positionList;
    }
    public void consumeEnd(ArrayList<Integer> positionList){
        emptyLock.lock();
        empty.addAll(positionList);
        firstOnEmpty.signal();
        emptyLock.unlock();
    }
}
