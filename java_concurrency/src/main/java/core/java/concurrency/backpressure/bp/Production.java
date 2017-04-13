package core.java.concurrency.backpressure.bp;

import java.util.*;
import java.util.concurrent.*;

public final class Production<T> implements AutoCloseable {

    private Queue<T> queue = new ConcurrentLinkedQueue<>();
    private boolean closed;

    private ExecutorService executor;
    private CompletionService<Void> completionService;

    public Production(final Worker<T> worker) {
        final List<Worker<T>> workers = new ArrayList<>();
        workers.add(worker);
        start(workers);
    }

    public Production(final List<Worker<T>> workers) {
        start(workers);
    }

    private void start(final List<Worker<T>> workers) {
        final Chief<T> chief = new Chief<>(this);
        executor = Executors.newWorkStealingPool();
        completionService = new ExecutorCompletionService<>(executor);
        workers.forEach(w -> {
            completionService.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    System.out.println("start to join " + w);
                    chief.join(w);
                    System.out.println("joined");
                    return null;
                }
            });
        });
    }

    public void addWork(final T work) {
        queue.add(work);
    }

    @Override
    public void close() throws Exception {
        closed = true;
        completionService.take();
        executor.shutdown();
    }

    protected synchronized List<T> produce(final long amount) {
        final List<T> list = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            final T work = queue.poll();
            if (work != null) {
                list.add(work);
            }
        }
        return list;
    }

    protected boolean isClosed() {
        return closed;
    }
}
