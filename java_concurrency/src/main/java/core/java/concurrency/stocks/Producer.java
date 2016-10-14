package core.java.concurrency.stocks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Producer extends Thread {

	private LoadHandler loadHandler;

	public Producer(LoadHandler loadHandler) {
		this.loadHandler = loadHandler;
	}

	@Override
	public void run() {
		generateUpdates();
	}

	public void generateUpdates() {
		Runnable producerTask = () -> {
			for (int i = 1; i < 100; i++) {
				loadHandler.receive(new PriceUpdate("Apple", 97.85, i));
				loadHandler.receive(new PriceUpdate("Google", 160.71, i));
				loadHandler.receive(new PriceUpdate("Facebook", 91.66, i));
				loadHandler.receive(new PriceUpdate("Google", 160.73, i));
				loadHandler.receive(new PriceUpdate("Facebook", 91.71, i));
				loadHandler.receive(new PriceUpdate("Google", 160.76, i));
				loadHandler.receive(new PriceUpdate("Apple", 97.85, i));
				loadHandler.receive(new PriceUpdate("Google", 160.71, i));
				loadHandler.receive(new PriceUpdate("Facebook", 91.63, i));
			}
		};
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
		executorService.scheduleWithFixedDelay(producerTask, 0, 3, TimeUnit.SECONDS);
	}

}
