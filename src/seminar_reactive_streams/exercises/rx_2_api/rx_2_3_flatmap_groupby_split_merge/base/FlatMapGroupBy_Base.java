package seminar_reactive_streams.exercises.rx_2_api.rx_2_3_flatmap_groupby_split_merge.base;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;


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
public class FlatMapGroupBy_Base {
	
	
	public static void main(String[] args) {
		
		System.out.println("FlatMapGroupBy_Base");
		
		FlatMapGroupBy_Base instance = new FlatMapGroupBy_Base();
				
		// flatMap() / groupBy() Pattern
		instance.flapMapGroupBySplitAndMerge();
	
	}


	
	/*
	 * flatMap() und groupBy() Ansatz
	 */
	void flapMapGroupBySplitAndMerge() {
		
		System.out.println("FlatMapGroupBy_Base.flapMapGroupBySplitAndMerge()");
		
		AtomicInteger selector = new AtomicInteger(0);
		Observable<Integer> source = Observable.range(0, 100);
		
		// TODO 1: Source In Gruppen aufspalten: .groupBy()
		// groupBy() erh�lt als Argument eine Funktion, die jedem Item einen Key zuordnet. Z.B. i -> selector.incrementAndGet() % 4
		
		// TODO 2 flatMap() benutzen, um die Gruppen wieder zusammenzuf�hren
		//.flatMap(group -> 
		
		// TODO 3: Elemente jeder Gruppe verarbeiten, innerhalb von flatMap(), z.B. mit group.map( value -> {})
			
		// TODO 4: Subscriber an flatMap() anschlie�en	

	}
	

}
