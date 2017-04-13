package core.java.concurrency.backpressure;

import java.util.ArrayList;
import java.util.List;

import core.java.concurrency.backpressure.bp.Production;
import core.java.concurrency.backpressure.bp.Worker;

public class Main {

    public static void main(final String[] args) throws Exception {

        final PrinterWorker w1 = new PrinterWorker("John");
        final PrinterWorker w2 = new PrinterWorker("Bill");
        final List<Worker<String>> list = new ArrayList<>();
        list.add(w1);
        list.add(w2);

        try (final Production<String> p = new Production<>(list)) {
            for (int i = 1; i <= 100; i++) {
                p.add("Item " + i);
            }
            System.out.println("1st batch done");
            Thread.sleep(2000);
            for (int i = 101; i <= 200; i++) {
                p.add("Item " + i);
            }
            System.out.println("2th batch done");
            Thread.sleep(1000);
            for (int i = 201; i <= 300; i++) {
                p.add("Item " + i);
            }
            System.out.println("3th batch done");
        }
    }
}
