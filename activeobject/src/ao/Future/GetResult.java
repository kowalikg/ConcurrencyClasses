package ao.Future;


public class GetResult implements Future {
    private boolean valuesGet;
    @Override
    public synchronized boolean isAvailable() {
        return valuesGet;
    }

    @Override
    public synchronized Object getResult() {
        return valuesGet;
    }

    // jezeli zadanie sie jeszcze nie skonczylo to sie powieś
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

    // jesli zadanie juz jest to sie powiadom
    public synchronized void setValues(boolean ready) {
        this.valuesGet = ready;
        if (this.valuesGet) this.notify();
    }
}
