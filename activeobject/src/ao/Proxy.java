package ao;

import ao.Future.Future;
import ao.Future.GetResult;
import ao.Future.PutResult;
import ao.Request.GetRequest;
import ao.Request.PutRequest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
// interfejs dla producentow i konsumentow do zlecania zadan
public class Proxy {
    private Scheduler scheduler;
    private Buffor buffor;

    public Proxy(int capacity){
        this.buffor = new Buffor(capacity);
        this.scheduler = new Scheduler();
        scheduler.start();
    }
    public Future put(int amount){
        PutResult newResult = new PutResult();
        PutRequest newRequest = new PutRequest(buffor, newResult, amount);
        //zakolejkowania requestu w schedulerze
        scheduler.enqueue(newRequest);
        return newResult;
    }
    public Future get(int amount){
        GetResult newResult = new GetResult();
        GetRequest newRequest = new GetRequest(buffor, newResult, amount);
        scheduler.enqueue(newRequest);
        return newResult;
    }
    public int getBufforCapacity(){
        return buffor.getCapacity();
    }
}
