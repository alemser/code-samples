package core.java.concurrency.travel;

import java.util.List;

public interface TripPlan {
	List<ServiceSupplier> getSuppliers(); 
    int getPrice(); 
    TripPlan combine(TripPlan tripPlan);
}
