package seminar_reactive_streams.exercises.rx_2_api.rx_2_5_back_pressure_basics.solution;


import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * Zeigen Sie, wie Back-Pressure das Verhalten eines Streams ver�ndert
 * 
 * Programmieren Sie dafuer  einen Stream mit folgenden Eigenschaften
 * - Stream Source erzeugt Integers 1 ... 100_000_000
 * - map() - Schritt bearbeitet die Daten
 * - observeOn() entkoppelt map() vom Subscriber
 * - Subscriber anmelden. Dieser ist langsamer als der Erzeuger (z.B. durch Thread.sleep())
 * - Fuegen Sie Shell-Ausschriften ein, um das Verhalten von map() und onNext() zu zeigen.
 * 
 * a) 	Die Stream Source ist ein Observable.
 *  	Zeigen Sie, dass Source und Subscriber beliebig weit auseinanderlaufen.
 * 
 * b) 	Die Stream Source ist ein Flowable.
 * 		Zeigen Sie, dass Source und Subscriber nur geringfuegig auseinander laufen, naemlich nie weiter als 128 Elemente.
 * 
 * Lernziel: Verhalten von Back-Pressure verstehen 
 *
 */
public class BackPressureBasics_Solution {
		
	
	public static void main(String[] args) {
		
		System.out.println("BackPressureBasics_Solution");
		
		BackPressureBasics_Solution instance = new BackPressureBasics_Solution();
		
		// a)
		//instance.a_ObservableNotBackPressureAware();
		
		// b)
		instance.b_FlowableBackPressureAware();		
	}

/*
	// Observable ralisiert keinen Back-Pressure 
	Observable<Integer> sourceObservable = Observable.range(0, 100_000_000);
	
	sourceObservable
	
	// TODO 1: map() Schritt
	
	// TODO 2:  observeOn() entkoppelt map()- Stufe von folgenden Stream-Stufen
	
	// TODO 3: Subscriber anmelden, und diesen ggf. ein bisschen verlagsamen .subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
	
*/
	
	/*
	 * a)
	 * 
	 * Bei der Verwendung von Observable f�hrt observeOn() zu einer unlimitierten Entkopplung des map() vom subscribe().
	 * 
	 * Da Observable nicht Back-Pressure aware ist laufen map() und subscribe() immer weiter auseinander,
	 * die Queue innerhalb der observeOn() Stream-Stagee muss immer mehr Elemente aufnehmen.
	 */
	void a_ObservableNotBackPressureAware() {
		
		System.out.println("b_ObservableNotBackPressureAware");
	
		// Observable realiseirt keinen Back-Pressure  
		Observable<Integer> sourceObservable = Observable.range(0, 100_000_000);
		
		sourceObservable
		
		// Map Schritt
		.map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
		
		// observeOn() observeOn() entkoppelt map()- Stufe von folgenden Stream-Stufen
		.observeOn(Schedulers.computation()) 
		
		// subscribe verarbeitet Elemnte deutlich nach dem Map-Schritt. Z.B. auf meiner Maschine:
		// map() 572034 Thread[main,5,main]
		// subscribe() 502374 Thread[RxComputationThreadPool-1,5,main]
		.subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
		
		
		// Hack to wait for completion of threads
		try {Thread.sleep(100000);} catch (InterruptedException e) {}
		
	}
	
	
	/*
	 * b)
	 * 
	 * Flowable ist Back-Pressure aware, daher laufen Publisher und Subscriber nur ca. 128 Elemente weit auseinader
	 * 
	 * observeOn() f�hrt zu einer Entkopplung des Generators von der restlichen Pipeline. Die beiden Teile sind dann 
	 * �ber eine Queue miteinander verbunden, die Elemente laufen auseinander.
	 * 
	 */
	void b_FlowableBackPressureAware() {
		
		System.out.println("b_FlowableBackPressureAware");
	
		//  Flowable realisert Back-Pressure 
		Flowable<Integer> sourceFlowable = Flowable.range(0, 100_000_000);
		
		sourceFlowable
		// this subscribeOn() is not necessarily required. But it enforces the Flowable to emitt in its own thread.
		.subscribeOn(Schedulers.computation())
		
		.map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
		
		// decouples Pipeline from Source-Observable
		.observeOn(Schedulers.computation()) 
		
		// subscribe is decoupeld from map, it consumes elements out of a buffer, eg. Element 0 is consumed, after Element 2 has been produced
		.subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
		
		
		// Hack to wait for completion of threads
		try {Thread.sleep(100000);} catch (InterruptedException e) {}
		
	}
	
}
