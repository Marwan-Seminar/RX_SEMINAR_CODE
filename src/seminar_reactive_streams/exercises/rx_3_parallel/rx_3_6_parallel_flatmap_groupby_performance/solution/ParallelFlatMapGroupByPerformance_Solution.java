package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_6_parallel_flatmap_groupby_performance.solution;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/* 
 * Hier wird gezeigt, dass druch die Parallelisierung mit Verzweigen und Zusammenfuerheren auf Basis 
 * von groupBy() und flatMap() ein signifikanter Performance-Vorteil erzielt werden kann.
 * 
 * Der Stream ruft im Map-Schritt eine CPU-intensive Funtktion auf, die ca. 100 Millisekunden lang laeuft.
 * 
 * Durch die Parallelisierung wird die Laufzeit ca. um den Faktor 4 auf einem Quadcore verringert.
 *   
 * Die Loesung  ist in drei Teile geliedert:
 * 
 * a) Der sequentielle Stream ohne Verzweigung (10 Sekunden)
 * b) Der Stream mit Verzweigung aber ohne Parallelitaet (10 Sekunden)
 * c) Der Stream mit Verzweigung und mit Parallelitaet (2 Sekunden)
 * 
 * Bemerkung: Die Zeitmessung ist in manchen Faellen nicht ganz akkurat,
 * da die CPU-Load simulierende Methode ungenau sein kann, im Falle von Preemption.
 */
 public class ParallelFlatMapGroupByPerformance_Solution {
	

	
	public static void main(String[] args) {
		
		System.out.println("ParallelFlatMapGroupByPerformance_Solution");
		
		ParallelFlatMapGroupByPerformance_Solution instance = new ParallelFlatMapGroupByPerformance_Solution();

		//a) Der sequentielle Stream ohne Verzweigung
		instance.a_serialStream();
		
		// b) Der Stream mit Verzweigung aber ohne Parallelitaet
		//instance.b_flapMapGroupBySequential();
		
		
		//  c) Der Stream mit Verzweigung und mit Parallelitaet
		instance.c_flapMapGroupByParallelObserveOn();
		
	}

	/*
	 * a) Der sequentielle Stream ohne Verzweigung
	 *  
	 * Es wird f�r jedes der 100 Elemente eine CPU-Intensive Methode aufgerufen, die ca. 100 Millisekunden l�uft.
	 * Die Laufzeit des Streams ist ca. 10 Sekunden. 
	 */
	void a_serialStream() {
		
		System.out.println("a_serialStream()");
		
		long startTime = System.currentTimeMillis();
		
		Observable.range(1,100).
			map( i -> {
				// CPU intensive call
		 		cpuIntesiveCall(100);
		 		return i;
			}).
			subscribe(System.out::println);
		
		System.out.println("a_serialStream() returns after " + (System.currentTimeMillis() - startTime) + " Milliseconds" );
		
	}

	/*
	 * Sequentielle Version des flatMap und groupBy Ansatzes.
	 * 
	 * Verzweigte Version des Streams in a), ohne Parallelitaet. Die Laufzeit ist aehnlich.
	 * fuer jedes der 100 Elemente eine CPU-Intensive Methode aufgerufen, die ca. 100 Millisekunden laeuft.
	 * Die Laufzeit des Algorithmus ist ca. 10 Sekunden. 
	 */
	void b_flapMapGroupBySequential() {
		System.out.println("a_flapMapGroupBySequential()");
		
		long startTime = System.currentTimeMillis();
		
		AtomicInteger selector = new AtomicInteger(0);
		
		Observable.range(0, 100)
		// Split into  Groups
		.groupBy(i -> selector.incrementAndGet() % 8)
		// flatMap
		.flatMap(group -> 
			// calling map() inside of flatMap() on each GroupedObservable.
			// This is the entry point for parallelization			
			group.map(j -> 	
			 	{
			 		System.out.println("Group " + group.getKey() + " map: " + j + " " + Thread.currentThread());
			 		// CPU intensive call
			 		cpuIntesiveCall(100);
			 		return j;
			 	}
			)
		)
		.subscribe(i -> {
			System.out.println("Subscriber got " + i + " " + Thread.currentThread());
		});
		
		System.out.println("a_flapMapGroupBySequential() returns after " + (System.currentTimeMillis() - startTime) + " Milliseconds" );
	}
	
	
	/*
	 * Parallele  Version des flatMap und groupBy Ansatzes
	 * In dieser Version wird das map() auf jedem GroupedObservable aufgerufen, innerhlab des map() 
	 * wird f�r jedes Item eine CPU-Intensive Funktion aufgerufen, die ca. 100 Millisekunden Sekunden l�uft.
	 * Die Laufzeit dieses Parallele Algorithmus ist auf meinem Quad-Core ca. 1,5 - 2,5 Sekunden je nach Anzahl der Groups. 
	 * Er ist damit Faktor 4 - 7 schneller, als der sequentielle Algorithmus. 
	 */
	void c_flapMapGroupByParallelObserveOn() {
		System.out.println("b_flapMapGroupByParallelObserveOn()");
		
		//AtomicInteger counter = new AtomicInteger();
		
		long startTime = System.currentTimeMillis();
		
		AtomicInteger selector = new AtomicInteger(0);
	
		//  map inside of flatMap()
		Observable.range(1, 100)
		// Split into  Groups
		.groupBy(i -> selector.incrementAndGet() % 8)
		// flatMap
		.flatMap(group -> 
			group
			// observeOn() is required here!
			// subscribeOn() does not work, as it affects the source Observable. Only "cached" emissions are processed in parallel with subscribeOn()
			.observeOn(Schedulers.computation()) // This assigns a thread to each GroupedObservable
			
			// calling map() inside of flatMap() for each GroupedObservable: Process each group for itself.
			// As each GroupedObservable has its own Thread to run on, the map calls are executed in parallel
			.map(j -> 				
			 	{
			 		System.out.println("Group " + group.getKey() + " map: " + j + " " + Thread.currentThread());
			 		// CPU intensive call
			 		cpuIntesiveCall(100);
			 		return j;
			 	}
			)
		)
		.subscribe(i -> {
			System.out.println("Subscriber: " + i + " " + Thread.currentThread() );
			if(i == 100) {
				// not correct, as emissions arrive out of order.
				System.out.println("Stream Ended after: " + (System.currentTimeMillis() - startTime) );
			}
		});	
		
		// TODO Wie mache ich hier die Zeitmessung?! Mein Kriterium oben stimmt nicht ganz, weil die Emissions out of order ankommen.
		
		
		// wait for threads to terminate
		sleep(10);
		



	
	}

	///////////////// HELPER ///////////////////////////////

	/*
	 * This method runs  
	 * runtimeInMillis
	 * Milliseconds, then it returns.
	 * CAVEAT: Returns even if the thread does not get enough CPU!
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
	
	/*
	 * Dieser Aufruf laueft ca. 100 Millisekunden auf (meiner) CPU 
	 */
	static void cpuIntensiveCall() {
		long start = System.currentTimeMillis();
		
		long dummy = 2;
		for(long counter = 0; counter <= Integer.MAX_VALUE / 5; counter ++) {
				dummy = (dummy + dummy);
				if (dummy > Integer.MAX_VALUE) {
					dummy = dummy % Integer.MAX_VALUE;
					
				}
				
		}
		
		long runtime = System.currentTimeMillis() - start;
		
		//System.out.println("cpuIntensiveCall: runtime " + runtime + " Thread: " + Thread.currentThread());
	}
	
	/*
	 * Dieser Aufruf blockert seconds Sekunden.
	 */
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
	
	///////////////// KURZE VARIANTE ///////////////////////////////
	private void flapMapGroupByParallelObserveOnSHORT() {
		System.out.println("flapMapGroupByParallelObserveOnSHORT()");
		
	
	
		//  map inside of flatMap()
		Observable.range(1, 100)
		// Split into  Groups
		.groupBy(i -> i % 8)
		// flatMap
		.flatMap(group -> 
			group.observeOn(Schedulers.computation()) // This assigns a thread to each GroupedObservable
			
			.map(j -> {
			   // CPU intensive call
			   cpuIntesiveCall(100);
			  return j;
			 })
		)
		.subscribe(i -> {}
		);	
		
		
	}

}
