package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_5_parallel_flatmap_groupby.solution;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/* 
 * RX 3.5 groupBy()-flatMap() ParallelisierungParallelisierung durch explizite Verzweigung 
 * Parallelisieren Sie den Stream Observable.range(1,100).map( i -> i).subscribe(System.out::println),
 * indem Sie Gruppen bilden, und diese Gruppen in eigenen Threads verarbeiten.
 * 
 * Zeigen Sie die Parallelit�t durch Shell-Ausgaben.
 * 
 * L�sungshinweis: Verwenden Sie folgende APIs
 * 
 * - gorupBy() zerlegt einen Stream in mehrere GroupedObservables
 * - flatMap() f�hrt die GroupedObservables wieder zu einem Observable zusammen
 * - observeOn() f�hrt jede diese Gruppen in einem eigenen Thread aus
 * 
 * Die L�sung ist in drei Teile gegliedert:
 * 
 * a) Der sequentielle Stream ohne Verzweigung
 * b) Der Stream mit Verzweigung aber ohne Parallelit�t
 * c) Der Stream mit Verzweigung und mit Parallelit�t
 */
public class ParallelFlatMapGroupBy_Solution {
	

	
	public static void main(String[] args) {
		
		System.out.println("ParallelFlatMapGroupBy_Solution");
		
		ParallelFlatMapGroupBy_Solution instance = new ParallelFlatMapGroupBy_Solution();
		
		// a)
		instance.a_serialStream();
		
		// b) 
		instance.b_flapMapGroupBySequential();
		
		
		// c)
		instance.c_flapMapGroupByParallelObserveOn();
		
	}

	/*
	 *  a) Der sequentielle Stream ohne Verzweigung
	 */
	void a_serialStream() {
		
		System.out.println("a_serialStream()");
		
		Observable.range(1,100).
			map( i -> i).
			subscribe(System.out::println);
		
	}


	/*
	 * b) Der Stream mit Verzweigung aber ohne Parallelit�t
	 * 
	 * Sequentielle Version des flatMap und groupBy Ansatzes.
	 */
	void b_flapMapGroupBySequential() {
		
		System.out.println("b_flapMapGroupBySequential()");
		
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
			 		return j;
			 	}
			)
		)
		
		.subscribe(i -> {
			System.out.println("Subscriber got " + i + " " + Thread.currentThread());
		});
		
		System.out.println("b_flapMapGroupBySequential() returns" );
	}
	
	
	/*
	 * c) Der Stream mit Verzweigung und mit Parallelit�t
	 *  
	 * Parallele  Version des flatMap und groupBy Ansatzes.
	 * Die PArallelit�t wird durch den Aufruf von observeOn() auf jeder der GroupedObservables 
	 * erreicht, die von groupBy() emittiert werden.
	 */
	void c_flapMapGroupByParallelObserveOn() {
		
		System.out.println("c_flapMapGroupByParallelObserveOn()");
				
		AtomicInteger selector = new AtomicInteger(0);
	
		//  map inside of flatMap()
		Observable.range(1, 100)
		// Split into  Groups
		.groupBy(i -> selector.incrementAndGet() % 8)
		// flatMap
		.flatMap(group -> 
			group
			// observeOn() is required here!
			// subscribeOn() does not work, as it affects the source Observable. Only "cached" emisstions are processed in parallel with subscribeOn()
			.observeOn(Schedulers.computation()) // This assigns a thread to each GroupedObservable
			
			// calling map() inside of flatMap() for each GroupedObservable: Process each group for itself.
			// As each GroupedObservable has its own Thread to run on, the map calls are executed in parallel
			.map(j -> 				
			 	{
			 		System.out.println("Group " + group.getKey() + " map: " + j + " " + Thread.currentThread());
			 		return j;
			 	}
			)
		)
		// Subscribe
		.subscribe(i -> {
			System.out.println("Subscriber: " + i + " " + Thread.currentThread() );
		});	
		
			
		// TODO HACK!!!
		sleep(10);
			
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
