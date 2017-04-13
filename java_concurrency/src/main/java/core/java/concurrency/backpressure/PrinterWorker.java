/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package core.java.concurrency.backpressure;

import java.util.List;
import java.util.Random;

import core.java.concurrency.backpressure.bp.Job;
import core.java.concurrency.backpressure.bp.Worker;

public class PrinterWorker implements Worker<String> {

    private Job job;
    private String name;

    public PrinterWorker(final String name) {
        this.name = name;
    }

    @Override
    public void join(final Job job) {
        System.out.println("joining");
        this.job = job;
        job.request(2);
    }

    @Override
    public void next(final List<String> work) {
        try {
            Thread.sleep(new Random().nextInt(10));
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        work.forEach(w -> System.out.println(name + " (" + w + ")"));

        job.request(2);
    }

    @Override
    public void completed() {
        System.out.println("Completed");
    }

    @Override
    public void error(final Throwable e) {
        System.out.println("Error");
    }
}
