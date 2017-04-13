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

            for (int i = 0; i < 200; i++) {
                p.addWork("Item " + i);
            }

        }

    }
}
