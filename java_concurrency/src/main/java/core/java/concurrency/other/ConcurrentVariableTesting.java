package core.java.concurrency.other;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentVariableTesting {
	public int counter;
	public volatile int volatileCounter;
	public AtomicInteger atomicCounter = new AtomicInteger(0);
	
	public ConcurrentVariableTesting() {
		Runnable taskA = () -> {
			counter++;
			volatileCounter++;			
			System.out.println("A atomicCounter " + atomicCounter.incrementAndGet());
			System.out.println("A counter " + counter);
			System.out.println("A volatileCounter " + volatileCounter);
		};
		
		Runnable taskB = () -> {
			counter++;
			System.out.println("B atomicCounter " + atomicCounter.incrementAndGet());
			System.out.println("B counter " + counter);
			System.out.println("B volatileCounter " + volatileCounter);
		};
		
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(taskA);
		service.submit(taskB);
		service.shutdown();
	}
	
	class IntHolder {
		public int counter;
	}
	
	public static void main(String[] args) {
		new ConcurrentVariableTesting();
	}
}
