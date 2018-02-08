package ao;
import ao.Request.MethodRequest;

public class Scheduler {
    private ActivationQueue queue = new ActivationQueue();

    public void enqueue(MethodRequest methodRequest) {
        queue.enqueue(methodRequest);
    }
    public void start(){
        Thread thread = new Thread(() -> {
            while(true) {
                MethodRequest methodRequest = queue.dequeue();
                methodRequest.execute();

            }
        });
        thread.start();
    }

}
