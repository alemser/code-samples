package core.java.concurrency.backpressure;

public interface Worker<T> {
	
	void onSubscribe(ProductOwner<T> subscription);
	
	void next(T work);
	
	void completed();
	
	void error();
}
