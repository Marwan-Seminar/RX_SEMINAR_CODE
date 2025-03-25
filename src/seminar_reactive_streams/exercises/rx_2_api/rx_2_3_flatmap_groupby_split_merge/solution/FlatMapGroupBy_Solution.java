package seminar_reactive_streams.exercises.rx_2_api.rx_2_3_flatmap_groupby_split_merge.solution;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;


/* Programmieren Sie einen Stream und gruppieren Sie dessen Daten mit groupBy() zu Gruppen.
 * Verarbeiten Sie jede dieser Gruppen.
 * F�gen Sie die Resultate mit flatMap() wieder zu einem einzigen Stream zusammen. 
 * 
 * Schrittweise L�sung
 * 
 * Erzeugen Sie mit Flowable.range(1,100) ein Flowable, das 100 Elemente emittiert.
 * Gruppieren Sie diese Elemente mit groupBy(i -> i% 4) in vier Gruppen
 * F�hren Sie eine triviale map-Operation auf den Elementen jeder Gruppe aus, z.B: map(i -> i)
 * Benutzen Sie flatMap() um die Gruppen wieder zusammenzuf�hren
 * Erzeugen Sie Shell-Output, der zeigt, dass jedes Element in genau einer Gruppe verarbeitet wird.
 * 
 * Lernziel: Verzweigen und Zusammenf�hren als Basis f�r die Parallelisierung von Streams kennen 
 * 
 */
public class FlatMapGroupBy_Solution {
	

	public static void main(String[] args) {

		System.out.println("FlatMapGroupBy_Solution");

		FlatMapGroupBy_Solution instance = new FlatMapGroupBy_Solution();


		// a) flatMap() / groupBy() Pattern
		instance.a_flapMapGroupBySplitAndMerge();

		// b) flatMap() / groupBy() Pattern mit expliziter Angabe der Return-Typen
		//instance.b_flapMapGroupByExplicitTypes();

	}


	
	/*
	 * Kurze Version des flatMap() und groupBy() Ansatzes
	 */
	void a_flapMapGroupBySplitAndMerge() {
		
		System.out.println("FlatMapGroupBy_Solution.a_flapMapGroupBySplitAndMerge()");
		
		AtomicInteger selector = new AtomicInteger(0);
		
		Observable.range(0, 100)
		// Split into  Groups
		.groupBy(i -> selector.incrementAndGet() % 4)
		// flatMap
		.flatMap(group -> 
			// calling map inside of flatMap(): // Process each group for itself.
			// This is the entry point for parallelization			
			group.map(value -> 	
			 	{
			 		System.out.println("Group " + group.getKey() + " map: " + value);
			 		return value;
			 	}
			)
		)
		.subscribe(i -> {
			System.out.println("Subscriber got " + i + " " + Thread.currentThread());
		});

	}
	
	/*
	 * Wie a) aber mit explizit typisierten Variablen f�r die Observables in der Pipeline
	 */
	void b_flapMapGroupByExplicitTypes() {

		System.out.println("FlatMapGroupBy_Solution.b_flapMapGroupByExplicitTypes()");

		AtomicInteger selector = new AtomicInteger(0);
		
		Observable<Integer> emitter = Observable.range(0, 100);
		
		// Split into Groups
		Observable<GroupedObservable<Object, Integer>> observableOfGroups =
				emitter.groupBy(i -> selector.incrementAndGet() % 8);
		
		// flatMap
		Observable<Integer> flattenedObservable = observableOfGroups.flatMap(group -> 
			// calling map inside of flatMap(): // Process each group for itself.
			// This is the entry point for parallelization			
			group.map(j -> 	
			 	{
			 		System.out.println("Group " + group.getKey() + " map: " + j);
			 		return j;
			 	}
			)
		);
		
		flattenedObservable.subscribe(System.out::println);	
	}

}	
