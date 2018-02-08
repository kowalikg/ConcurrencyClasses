package com.company;

public class Main {

    public static void main(String[] args) {
        int N = 10;
        Buffor buffor = new Buffor(20);
        Thread[] producers = new Thread[N];
        Thread[] consumers = new Thread[N];

        for (int i = 0; i < N; i++){
            producers[i] = new Thread(new Producer(i, buffor));
            consumers[i] = new Thread(new Consumer(i, buffor));
        }
        for (int i = 0; i < N; i++){
            producers[i].start();
            consumers[i].start();
        }
        for (int i = 0; i < N; i++){
            try {
                consumers[i].join();
                producers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
