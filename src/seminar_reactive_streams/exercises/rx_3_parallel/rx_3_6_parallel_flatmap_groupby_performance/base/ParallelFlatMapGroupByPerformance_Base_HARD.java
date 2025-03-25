package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_6_parallel_flatmap_groupby_performance.base;

import io.reactivex.rxjava3.core.Observable;

/* 
 * Parallelisieren Sie einen Observable-Stream, und zegen Sie, dass dadurch die Ausf�hrung einer CPU-intensiven Berechnung wesentlich beschleunigt werden kann. 
 * a) Stream in mehrere unabh�ngige Sub-Streams zerlegen
 * b) Die Sub-Streams parallel ausf�hren.
 * c) CPU-intensive Methode f�r jedes Stream-Element ausf�hren, Beschleunigung zeigen
 * 
 * L�sungshinweise
 * gorupBy() zerlegt einen Stream in mehrere GroupedObservables
 * flatMap() f�hrt die GroupedObservables wieder zu einem Observable zusammen
 * observeOn() f�hrt jede diese Gruppen in einem eigenen Thread aus
 * 
 * Lernziel: Parallelisierung von Streams verstehen, Performance-Potential erkennen.
 * 
 */
public class ParallelFlatMapGroupByPerformance_Base_HARD {
	
	
	public static void main(String[] args) {
		
		System.out.println("ParallelFlatMapGroupBy_Base");
		
		ParallelFlatMapGroupByPerformance_Base_HARD instance = new ParallelFlatMapGroupByPerformance_Base_HARD();
		
		// Base code for the flatMap() / groupBy() Pattern with CPU-intensive call for each item in the stream
		instance.flapMapGroupByPerformance_BASE();
		
		
	}


	/* 
	 * Bauanleitung f�r 
	 * 
	 * a) Sequentielle Version des flatMap() / groupBy() Ansatzes, um einen Stream zu zerteilen und wieder zusammenzuf�hren 
	 * b) Parallelelisierung dieses Ansatzes mit observeOn()
	 *
	 */
	void flapMapGroupByPerformance_BASE() {
		
		System.out.println("flapMapGroupByPerformance_BASE()");
		
		// Ingredients:
				// 1. groupBy(i -> i%8)
				// 2. flatMap( group -> ....)
				// 3. map(i -> {cpuIntesiveCall(100); return j;})
					
				// 4. finally, when the flatmMap()-groupBy() algorithm is running, find the right place to apply 
				// observeOn(Schedulers.computation())
				// so that each group is processed in its own thread
				
		
		// Algorithm
		
			// Source Observable
			Observable.range(1, 100);
			
			// TODO 1 Split into  Groups
			
			// TODO 2 use flatMap()
			
			// TODO LATER use observeOn()
				
			// TODO 3 apply map() per group and call CPU-intensive method
				
			// TODO subscribe	
	}
	

		

	///////////////// HELPER ///////////////////////////////

	/*
	 * This method runs  
	 * runtimeInMillis
	 * Milliseconds on a CPU without blocking, then it returns.
	 */
	private static void cpuIntesiveCall(long runtimeInMillis) {
		 long start = System.currentTimeMillis();
		 long dummy = 3;
		 while(true) {
			 dummy = (dummy + dummy);
			 if (dummy > Integer.MAX_VALUE) {
				 dummy = dummy %  Integer.MAX_VALUE;
				 if (System.currentTimeMillis() - start > runtimeInMillis){
					 return;
				 }
			 }
			 

		 }
	 }
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
}