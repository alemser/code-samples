package core.java.concurrency.stocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoadHandler {

	private static final int MAX_PRICE_UPDATES = 100;
	private static final int MAX_BUFFER = 2000;
	private final Consumer consumer;
	private List<PriceUpdate> priceUpdates = Collections.synchronizedList(new ArrayList<>());

	public LoadHandler(Consumer consumer) {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
		executorService.scheduleWithFixedDelay(new Sender(), 0, 1, TimeUnit.SECONDS);
		this.consumer = consumer;
	}

	public void receive(PriceUpdate priceUpdate) {
		if (priceUpdates.size() <= MAX_BUFFER) {
			priceUpdates.add(0, priceUpdate);
		}
	}

	class Sender implements Runnable {
		public void run() {
			List<PriceUpdate> recentUpdates = priceUpdates.stream().limit(MAX_PRICE_UPDATES)
					.collect(Collectors.toList());
			consumer.send(recentUpdates);
			priceUpdates.removeAll(recentUpdates);
			System.out.println("Remaining size: " + priceUpdates.size());

		}
	}
}
