package core.java.concurrency.backpressure;

import java.util.List;

final class ProductOwner<T> {
	private Production production;
	
	public ProductOwner(Production production) {
		this.production = production;
	}
	
	private List<Worker<T>> subscribers;
	
	public T request(T work) {
		return null;
	}
	
}
