package seminar_reactive_streams.exercises.rx_2_api.rx_2_4_groupby_standalone.base;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;


/*
 * Teilen Sie einen Stream mittels groupBy() in Gruppen auf, und verarbeiten Sie die Elemente dieser Gruppen.
 * (Das ist in Isolation zwar nicht sinnvoll, aber es erleichtert das Verst�ndnis von groupBy())
 * 
 * Schrittweise L�sung
 * 
 * - Gruppieren Sie diese Elemente eines Streams mit groupBy(i -> i% 4) in vier Gruppen
 * - F�hren Sie eine triviale map-Operation auf den Elementen jeder Gruppe aus, z.B: map(i -> i)
 * - Erzeugen Sie Shell-Output, der zeigt, dass jedes Element in genau einer Gruppe verarbeitet wird.
 * - Subskribieren Sie Dummy-Subscriber an den Gruppen
 * - Subskribieren Sie einen Subscriber an groupBy(), um den Ablauf zu starten
 * 
 * Lernziel:Verstehen, wie groupBy() in Isolation funktioniert, um die Leistung von flatMap() zu erkennen* 
 *  
 */
public class GroupByStandalone_Base {
	

	public static void main(String[] args) {

		System.out.println("GroupByStandalone_Base");

		GroupByStandalone_Base instance = new GroupByStandalone_Base();

		// groupBy() ohne flatMap(): nicht sinnvoll, aber demonstriert das groupBy() Verhalten
		instance.groupByOhneFlatMap();

	}



	/*
	 * Hier wird gezeigt, wie der groupBy() Operator aufgerufen werden kann. 
	 * Eigentlich wird groupBy() zusammen mit flatMap() benutzt
	 * 
	 * Ich benutze hier jedoch kein flatMap(). 
	 * Der Code ist dadurch inhaltlich nicht sinnvoll, aber er macht deutlich, 
	 * was flatMap() leistet. 
	 * 
	 */
	void groupByOhneFlatMap(){

		System.out.println("FlatMapGroupBy_Base.groupByOhneFlatMap()");

		Observable<Integer> source = Observable.range(0, 10);

		// TODO 1: groupBy() auf die Source anwenden, z.B. mit  = i -> i% 4 als Argument
		
		// groupBy() liefert ein Observable<GroupedObservable> zur�ck. 
		Observable<GroupedObservable<Integer, Integer>> observableOfGroups; 

		// TODO "�usseres map()": Einen Subscriber an das  Observable<GroupedObservable> anmelden, und jedes GroupedObservable verarbeiten mit map()
		//observableOfGroups.map(group -> {
			// TODO "Inneres map()" jedes Element eines GroupedObservable mit map() verarbeiten
			//group.map( mapValue -> { 
				
			// TODO "Inneres" Subscribe: Ohne dieses wird das innere map() nicht ausgef�hrt
			// .subscribe(value ->{

		// TODO "�usseres" Subscribe startet den Ablauf 
		//.subscribe( key -> {
			
	}
}	
