package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_6_parallel_flatmap_groupby_performance.base;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/* 
 * Hier wird gezeigt, dass durch die Parallelisierung mit Verzweigen und Zusammenf�rheren auf Basis 
 * von groupBy() und flatMap() ein signifikanter Performance-Vorteil erzielt werden kann.
 * 
 * Der Stream ruft im Map-Schritt eine CPU-intensive Funtktion auf, die ca. 100 Millisekunden lang l�uft.
 * 
 * Durch die Parallelisierung soll es Ihnen gelingen, die Laufzeit ca. um den Faktor 4 auf einem Quadcore zu verringern.
 *  
 * --->>> Wenn Sie es sich leicht machen wollen, arbeiten Sie nur in Teil c. <<<---  
 *  
 * Ansonsten: Gliedern Sie die L�sung in drei Teile:
 * 
 * a) 	Der sequentielle Stream ohne Verzweigung (ca. 10 Sekunden Laufzeit). 
 * 		Dieser Teil liegt schon fertig vor, in a_serialStream()
 * 
 * b) 	Der Stream mit Verzweigung aber ohne Parallelit�t (ca. 10 Sekunden Laufzeit).
 *  	Dieser Teil liegt als Ger�st vor, in b_flapMapGroupBySequential()
 * 
 * c) 	Der Stream mit Verzweigung und mit Parallelit�t (ca. 2 Sekunden Laufzeit).
 *  	Dieser Teil liegt weitgehend fertig vor in c_flapMapGroupByParallelObserveOn(),
 *   	Sie m�ssen lediglich die Parallelit�t einbauen.
 */
 public class ParallelFlatMapGroupByPerformance_Base_EASY {
	

	
	public static void main(String[] args) {
		
		System.out.println("ParallelFlatMapGroupByPerformance_Solution");
		
		ParallelFlatMapGroupByPerformance_Base_EASY instance = new ParallelFlatMapGroupByPerformance_Base_EASY();

		//a) Der sequentielle Stream ohne Verzweigung
		instance.a_serialStream();
		
		// b) Der Stream mit Verzweigung aber ohne Parallelit�t
		instance.b_flapMapGroupBySequential();
		
		
		//  c) Der Stream mit Verzweigung und mit Parallelit�t
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
	 * b) Sequentielle Version des flatMap und groupBy Ansatzes.
	 * 
	 * Bauen Sie den obigen Stream so um, dass er mit flatMap() und groupBy()
	 * verzweigt und zusammengef�hrt wird.
	 * 
	 *  
	 */
	void b_flapMapGroupBySequential() {
		System.out.println("b_flapMapGroupBySequential()");
		
		long startTime = System.currentTimeMillis();
		
		AtomicInteger selector = new AtomicInteger(0);
		
		// TODO 1 In Gruppen aufteilen: .groupBy(i -> selector.incrementAndGet() % 8)
		
		// TODO 4 Subscriber anmelden: .subscribe(i -> ...)
		
		Observable.range(0, 100)
		
 		// TODO 1 In Gruppen aufteilen: .groupBy(i -> selector.incrementAndGet() % 8)
		//.groupBy(i -> selector.incrementAndGet() % 8)
		
		// TODO 2 Die Gruppen zusammenf�hren: .flatMap(group -> ...
		//.flatMap(group -> 
			// calling map() inside of flatMap() on each GroupedObservable. This is the entry point for parallelization			
		
			// TODO 3 Innerhalb von flatMap() jedes Gruppenelement verarbeiten: group.map(...)
			//group.map(j -> 	
			 	//{
			 		//System.out.println("Group " + group.getKey() + " map: " + j + " " + Thread.currentThread());
			 		// CPU intensive call
			 		//cpuIntesiveCall(100);
			 		//return j;
			 	//}
			//)
		//)
		
		// TODO 4 Subscriber anmelden: .subscribe(i -> ...)
		.subscribe(i -> {
			System.out.println("Subscriber got " + i + " " + Thread.currentThread());
		});
		
		System.out.println("b_flapMapGroupBySequential() returns after " + (System.currentTimeMillis() - startTime) + " Milliseconds" );
	}
	
	
	/*
	 * c) Parallele  Version des flatMap und groupBy Ansatzes.
	 * 
	 * Hier erhlaten Sie als Ausgangsbasis bereits den korrekt verzweigten und zusammengef�hrten Ansatz.
	 * 
	 * Ihre Aufgabe ist es, die Parallelit�t einzubauen. 
	 * 
	 * Sie erreichen dies durch den Aufruf von observeOn() auf jedem der GroupedObservables, 
	 * die von groupBy() emittiert werden.
	 *  
	 */
	void c_flapMapGroupByParallelObserveOn() {
		System.out.println("c_flapMapGroupByParallelObserveOn()");
		
		//AtomicInteger counter = new AtomicInteger();
		
		long startTime = System.currentTimeMillis();
		
		AtomicInteger selector = new AtomicInteger(0);
	
		// TODO: an der richtigen Stelle den folgenden Aufruf zur Parallelisierung einbringen:
		// .observeOn(Schedulers.computation()) 
		
		//  map inside of flatMap()
		Observable.range(1, 100)
		// Split into  Groups
		.groupBy(i -> selector.incrementAndGet() % 8)
		// flatMap
		.flatMap(group -> 
			group
			
			//.observeOn(Schedulers.computation()) // This assigns a thread to each GroupedObservable
			
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
				System.out.println("Parallelized Stream Completed after: " + (System.currentTimeMillis() - startTime) );
			}
		});	
		
		// TODO Wie mache ich hier die Zeitmessung?! Mein Kriterium oben stimmt nicht ganz, weil die Emissions out of order ankommen.
		
		
		// waiting for other threads to terminate
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
