package core.java.concurrency.backpressure;

import core.java.concurrency.backpressure.bp.Job;
import core.java.concurrency.backpressure.bp.Worker;

public class PrinterWorker implements Worker<String> {

    private Job job;
    private String name;
    private int count;

    public PrinterWorker(final String name) {
        this.name = name;
    }

    @Override
    public void join(final Job job) {
        this.job = job;
        job.request();
    }

    @Override
    public void next(final String work) {
        count++;
        job.request();
    }

    @Override
    public void completed() {
        System.out.println(name + " has completed it's jobs (" + count + " items)");
    }

    @Override
    public void error(final Throwable e) {
        System.out.println(name + " got an error");
    }

    @Override
    public String getName() {
        return name;
    }
}
