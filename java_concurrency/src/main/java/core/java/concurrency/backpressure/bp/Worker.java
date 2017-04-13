package core.java.concurrency.backpressure.bp;

public interface Worker<T> {

    void join(Job job);

    void next(T work);

    void completed();

    void error(Throwable e);

    String getName();
}
