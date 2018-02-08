package asyn;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Buffor {
    private int capacity;
    public Buffor(int capacity){
        this.capacity = capacity;
    }
    public void push(Producer p, ArrayList<Integer> positionList){
        int counter = 1;
        Values.portionsProduced.addAndGet(positionList.size());
      /*  for (Integer position: positionList){
            System.out.println("Producer " + p.getId() + " push to " + position +
                    " : " + counter++ + " from " + positionList.size() );
        }*/
        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void take(Consumer c, ArrayList<Integer> positionList){
        int counter = 1;
        /*for (Integer position: positionList){
            System.out.println("Consumer " + c.getId() + " takes from " + position +
                    " : " + counter++ + " from " + positionList.size() );
        }*/

        try {
            TimeUnit.MILLISECONDS.sleep(Values.threadDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCapacity() {
        return capacity;
    }
}
