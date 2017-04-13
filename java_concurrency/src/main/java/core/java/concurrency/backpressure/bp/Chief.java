package core.java.concurrency.backpressure.bp;

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
        public void request() {
            T work = production.get();
            if (work == null && production.isClosed()) {
                System.out.println("nothing more to " + worker.getName());
                worker.completed();
                return;
            }

            while (work == null && !production.isClosed()) {
                try {
                    System.out.println(worker.getName() + " is waiting for work...");
                    Thread.sleep(100);
                    work = production.get();
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (work == null) {
                System.out.println("nothing more to " + worker.getName());
                worker.completed();
                return;
            }

            try {
                worker.next(work);
            } catch (final Exception e) {
                worker.error(e);
            }
        }
    }

}
