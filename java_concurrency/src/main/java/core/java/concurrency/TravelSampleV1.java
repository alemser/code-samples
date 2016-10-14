package core.java.concurrency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import core.java.concurrency.travel.AerLingus;
import core.java.concurrency.travel.Hertz;
import core.java.concurrency.travel.Ibis;
import core.java.concurrency.travel.Lufthansa;
import core.java.concurrency.travel.Ryanair;
import core.java.concurrency.travel.ServiceSupplier;
import core.java.concurrency.travel.TripPlan;

/**
 * Example of combining the completable futures.
 * Based on article: https://www.infoq.com/articles/Functional-Style-Callbacks-Using-CompletableFuture.
 */
public class TravelSampleV1 {
	Executor executor = Executors.newWorkStealingPool();
	List<ServiceSupplier> airlines = new ArrayList<>();
	List<ServiceSupplier> hotels = new ArrayList<>();
	List<ServiceSupplier> cars = new ArrayList<>();

	private TripPlan selectBestTripPlan(List<ServiceSupplier> serviceList) {
		List<CompletableFuture<TripPlan>> tripPlanFutures = serviceList.stream()
				.map(svc -> CompletableFuture.supplyAsync(svc::createPlan, executor)).collect(Collectors.toList());

		return tripPlanFutures.stream().min(Comparator.comparing(cf -> cf.join().getPrice())).get().join();
	}

	private TripPlan selectBestTripPlan(List<ServiceSupplier> serviceList, String favoredAlliance) {
		List<CompletableFuture<TripPlan>> tripPlanFutures = serviceList.stream()
				.filter(ts -> favoredAlliance == null || ts.getAlliance().equals(favoredAlliance))
				.map(svc -> CompletableFuture.supplyAsync(svc::createPlan, executor)).collect(Collectors.toList());

		return tripPlanFutures.stream().min(Comparator.comparing(cf -> cf.join().getPrice())).get().join();
	}

	private TripPlan addCarHire(TripPlan p) {
		List<String> alliances = p.getSuppliers().stream().map(ServiceSupplier::getAlliance).distinct()
				.collect(Collectors.toList());
		if (alliances.size() == 1) {
			return p.combine(selectBestTripPlan(cars, alliances.get(0)));
		} else {
			return p.combine(selectBestTripPlan(cars));
		}
	}

	public TravelSampleV1() throws Exception {
		airlines.add(new AerLingus());
		airlines.add(new Ryanair());
		airlines.add(new Lufthansa());
		cars.add(new Hertz());
		hotels.add(new Ibis());
		
		TripPlan tripPlan = CompletableFuture.supplyAsync(() -> selectBestTripPlan(airlines))
				.thenCombine(CompletableFuture.supplyAsync(() -> selectBestTripPlan(hotels)), TripPlan::combine)
				.thenCompose(p -> CompletableFuture.supplyAsync(() -> addCarHire(p))).get();
		System.out.println(tripPlan);
		System.out.println(tripPlan.getSuppliers());
	}

	public static void main(String[] args) throws Exception {
		new TravelSampleV1();
	}
}
