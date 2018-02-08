package ao.Future;

public interface Future {
    boolean isAvailable();
    Object getResult();
    void waitForComplete();


}
