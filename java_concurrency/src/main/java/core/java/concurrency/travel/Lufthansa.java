package core.java.concurrency.travel;

import java.util.ArrayList;
import java.util.List;

public class Lufthansa implements ServiceSupplier {

	@Override
	public TripPlan createPlan() {
		TripPlan tripPlan = new TripPlan() {
			private List<ServiceSupplier> suppliers = new ArrayList<>();
			private int price = 1250;
			
			@Override
			public List<ServiceSupplier> getSuppliers() {
				return suppliers;
			}
			
			@Override
			public int getPrice() {
				return price;
			}
			
			@Override
			public TripPlan combine(TripPlan tripPlan) {
				suppliers.addAll(tripPlan.getSuppliers());
				price += tripPlan.getPrice();
				return this;
			}
			
			@Override
			public String toString() {
				return "Lufthansa: " + getPrice();
			}
		};
		return tripPlan;
	}

	@Override
	public String getAlliance() {
		return "LuftGroup";
	}

}
