package core.java.concurrency.backpressure.bp;

import java.util.List;

public interface Worker<T> {

    void join(Job job);

    void next(List<T> work);

    void completed();

    void error(Throwable e);
}
