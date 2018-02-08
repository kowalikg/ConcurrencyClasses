package los;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Values.threadDelay = Integer.parseInt(args[0]);
        Values.jobs = Integer.parseInt(args[1]);
        int N = 2;
        int bufferSize = 20;
        Buffor buffor = new Buffor(bufferSize);
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
        for (int i = 1 ; i <= 20; i ++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Values.threadDelay + "," + Values.jobs + "," + i + "," +
                    Values.portionsLaunched + "," + Values.portionsProduced + "," + Values.jobsDone);
        }


        System.exit(0);
    }
}
