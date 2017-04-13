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
        if (workers.isEmpty()) {
            throw new RuntimeException("Can't start production without workers");
        }
        System.out.println("Starting production with " + workers.size() + " workers");
        final Chief<T> chief = new Chief<>(this);
        executor = Executors.newFixedThreadPool(2);
        completionService = new ExecutorCompletionService<>(executor);
        workers.forEach(w -> completionService.submit(() -> chief.join(w), null));
        executor.shutdown();
    }

    public void add(final T work) {
        queue.add(work);
    }

    @Override
    public void close() throws Exception {
        System.out.println("closing down production with " + queue.size() + " items left");
        closed = true;
        final long ini = System.currentTimeMillis();
        System.out.println("Waiting for the workers to finish");
        completionService.take();
        System.out.println(System.currentTimeMillis() - ini + " ms take to finish after closing down. Now " + queue.size() + " items left");
        executor.shutdown();
        System.out.println("all workers done...");
    }

    protected T get() {
        return queue.poll();
    }

    protected boolean isClosed() {
        return closed;
    }
}
