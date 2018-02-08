package ao;

import ao.Request.MethodRequest;
import ao.Request.PutRequest;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ActivationQueue {
    //kolejki requestow
    private Queue<MethodRequest> producersQueue = new ArrayDeque<>();
    private Queue<MethodRequest> consumersQueue = new ArrayDeque<>();

    public synchronized MethodRequest dequeue() {
        MethodRequest methodRequest;
        do {
            methodRequest = getNextRequest();
            if (methodRequest == null){ // jeśli nie możemy nic wykonać to powieśmy się i poczekajmy na jakieś nowe żądanie
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        while(methodRequest == null);
        return methodRequest;
    }

    public synchronized void enqueue(MethodRequest methodRequest) {
        boolean notification = false;
        if (producersQueue.isEmpty() && consumersQueue.isEmpty()) notification = true; // jesli obie koljeki byly puste to powiadamiamy siebie ze sie cos dodalo
        // jezeli kolejka do ktorej wstawiamy jest pusta a druga kolejka nie ale nie mozna wykonac żądania z niej to również musimy się powiadomić
        if (methodRequest instanceof PutRequest) {
            if (producersQueue.isEmpty() && !consumersQueue.isEmpty() && !consumersQueue.peek().guard()) notification = true;
            producersQueue.add(methodRequest);
        }
        else{
            if (consumersQueue.isEmpty() && !producersQueue.isEmpty() && !producersQueue.peek().guard()) notification = true;
            consumersQueue.add(methodRequest);
        }
        if (notification) this.notify();
    }

    private MethodRequest getNextRequest() {
        if (!producersQueue.isEmpty() && producersQueue.peek().guard())
            return producersQueue.poll();
        if (!consumersQueue.isEmpty() && consumersQueue.peek().guard())
            return consumersQueue.poll();
        return null;
    }
}