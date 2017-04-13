package core.java.concurrency.other;

import java.util.Random;
import java.util.concurrent.FutureTask;

/**
 * From Java Concurrency in Practice.
 */
public class FutureTaskSample {

	private final FutureTask<ProductInfo> future = new FutureTask<ProductInfo>( () -> {
		long time = new Random().nextInt(5) * 1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return new ProductInfo("Product", (int) time);			
	});

	private final Thread thread = new Thread(future);

	public void start() {
		thread.start();
	}

	public ProductInfo get() throws InterruptedException {
		try {
			return future.get();
		} catch (InterruptedException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		FutureTaskSample fts = new FutureTaskSample();
		fts.start();
		System.out.println("do smothing while obtaining product info...");
		System.out.println(fts.get());
	}
}

class ProductInfo {
	private final String productName;
	private final Integer qtAvailable;

	public ProductInfo(String productName, Integer qtAvailable) {
		this.productName = productName;
		this.qtAvailable = qtAvailable;
	}

	@Override
	public String toString() {
		return productName + " (" + qtAvailable + ")";
	}
}