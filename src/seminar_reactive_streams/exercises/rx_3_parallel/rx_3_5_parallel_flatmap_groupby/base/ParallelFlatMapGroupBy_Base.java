package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_5_parallel_flatmap_groupby.base;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;

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
public class ParallelFlatMapGroupBy_Base {
	

	
	public static void main(String[] args) {
		
		System.out.println("ParallelFlatMapGroupBy_Base");
		
		ParallelFlatMapGroupBy_Base instance = new ParallelFlatMapGroupBy_Base();
		
		// a) 
		instance.a_serialStream();
		
		// b) 
		instance.b_flapMapGroupBySequential();
		
		// c) 
		instance.c_flapMapGroupByParallelObserveOn();
		
	}

	/*
	 * a) Der sequentielle Stream ohne Verzweigung
	 */
	void a_serialStream() {
		
		System.out.println("a_serialStream()");
		
		Observable.range(1,100).
			map( i -> i).
			subscribe(System.out::println);
		
	}


	/*
	 * b) Sequentielle Version des flatMap und groupBy Ansatzes.
	 * 
	 * Bauen Sie den obigen Stream so um, dass er mit flatMap() und groupBy()
	 * verzweigt und zusammengef�hrt wird.
	 */
	void b_flapMapGroupBySequential() {

		System.out.println("b_flapMapGroupBySequential()");

		AtomicInteger selector = new AtomicInteger(0);

		// Source Observable
		Observable.range(1, 100);

		// TODO 1 In Gruppen aufteilen: .groupBy(i -> selector.incrementAndGet() % 8)

		// TODO 2 Die Gruppen zusammenf�hren: .flatMap(group -> ...

		// TODO 3 Innerhalb von flatMap() jedes Gruppenelement verarbeiten: group.map(...)

		// TODO 4 Subscriber anmelden: .subscribe(i -> ...)

		System.out.println("b_flapMapGroupBySequential() returns");
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
		
		
		
		// 10 Sekunden warten, bis das Beispiel beendet ist (HACK!!!)
		sleep(10);


	
	}

	///////////////// HELPER ///////////////////////////////

		
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}

}
