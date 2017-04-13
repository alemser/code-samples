package core.java.concurrency.backpressure.bp;

public interface Job {

    void request(long amount);

}
