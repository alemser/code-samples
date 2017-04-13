package core.java.concurrency.other;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * From Java Concurrency in Practice.
 */
public class CountDownLatchSample {
	
	public void timeTasks(int nThreads, final Runnable task) throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThreads);
		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread() {
				public void run() {
					try {
						startGate.await();
						try {
							task.run();
						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			};
			t.start();
		}
		
		System.out.println(nThreads + " were started but it will run only after the startGate.countDown()");
		Thread.sleep(1000);//simulation some processing...
		startGate.countDown();
		System.out.println(nThreads + " should be running and we will wait for completion...");
		endGate.await();
		System.out.println("All " + nThreads + " ran");
	}
	
	public static void main(String[] args) throws InterruptedException {
		Runnable task = () -> {			
			try {
				Thread.sleep(new Random().nextInt(5)*1000);
				System.out.println("task ran successfuly..." + Thread.currentThread().getId());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};
		new CountDownLatchSample().timeTasks(4, task);
	}
}
