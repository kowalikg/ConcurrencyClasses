package ao.Future;

public class PutResult implements Future {
    private boolean state;
    @Override
    public synchronized boolean isAvailable() {
        return state;
    }

    @Override
    public synchronized Object getResult() {
        return null;
    }

    @Override
    public synchronized void waitForComplete() {
        while (!isAvailable()){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setState(boolean state) {
        this.state = state;
        if(state) this.notify();
    }
}
