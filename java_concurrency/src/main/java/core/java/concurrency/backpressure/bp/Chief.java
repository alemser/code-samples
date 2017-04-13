package core.java.concurrency.backpressure.bp;

import java.util.List;

public final class Chief<T> {
    private Production<T> production;

    protected Chief(final Production<T> production) {
        this.production = production;
    }

    public void join(final Worker<T> worker) {
        worker.join(new ManagedJob(worker));
    }

    class ManagedJob implements Job {
        private Worker<T> worker;

        public ManagedJob(final Worker<T> worker) {
            this.worker = worker;
        }

        @Override
        public void request(final long amount) {
            try {
                List<T> work = askProduction(amount);
                if (work != null) {
                    worker.next(work);
                    return;
                }

                if (production.isClosed()) {
                    System.out.println("work complete because the production is closed");
                    worker.completed();
                    return;
                }

                work = askProduction(amount);

                if (work == null) {
                    System.out.println("work complete because the production was closed already");
                    worker.completed();
                    return;
                }

                worker.next(work);
            } catch (final Exception e) {
                e.printStackTrace();
                worker.error(e);
            }
        }
    }

    private List<T> askProduction(final long amount) {
        List<T> work = production.produce(amount);
        if (work == null && !production.isClosed()) {
            try {
                Thread.sleep(1);
                work = production.produce(amount);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return work;
    }
}
