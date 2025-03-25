package seminar_reactive_streams.exercises.rx_2_api.rx_2_5_back_pressure_basics.base;

/*
 * Zeigen Sie, wie Back-Pressure das Verhalten eines Streams ver�ndert
 * 
 * Programmieren Sie daf�r  einen Stream mit folgenden Eigenschaften
 * - Stream Source erzeugt Integers 1 ... 100_000_000
 * - map() - Schritt bearbeitet die Daten
 * - observeOn() entkoppelt map() vom Subscriber
 * - Subscriber anmelden. Dieser ist langsamer als der Erzeuger (z.B. durch Thread.sleep())
 * - F�gen Sie Shell-Ausschriften ein, um das Verhalten von map() und onNext() zu zeigen.
 * 
 * a) 	Die Stream Source ist ein Observable.
 *  	Zeigen Sie, dass Source und Subscriber beliebig weit auseinanderlaufen.
 * 
 * b) 	Die Stream Source ist ein Flowable.
 * 		Zeigen Sie, dass Source und Subscriber nur geringf�gig auseinander laufen, n�mlich nie weiter als 128 Elemente.
 * 
 * Lernziel: Verhalten von Back-Pressure verstehen 
 *
 */
public class BackPressureBasics_Base {
		
	
	public static void main(String[] args) {
		
		System.out.println("BackPressureBasics_Base");
		
		BackPressureBasics_Base instance = new BackPressureBasics_Base();
		
		// a)
		instance.a_ObservableNotBackPressureAware();
		
		// b)
		//instance.b_FlowableBackPressureAware();		
		
		// Hack to wait for completion of threads
		try {Thread.sleep(100000);} catch (InterruptedException e) {}

	}

/*
	
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
		
		System.out.println("BackPressureBasics_Base.a_ObservableNotBackPressureAware()");
	
		// TODO 1: Observable anlegen, dieses  ralisiert keinen Back-Pressure: Observable<Integer> sourceObservable = Observable.range(0, 100_000_000);
		
		// TODO 2: map() Schritt: map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
		
		// TODO 3: observeOn() entkoppelt map()- Stufe von folgenden Stream-Stufen
		
		// TODO 4: Subscriber anmelden, und diesen ggf. ein bisschen verlagsamen subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
		
				
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
		
		System.out.println("BackPressureBasics_Base.b_FlowableBackPressureAware");
	
		// TODO 1 Flowable anlegen, dieses realisert Back-Pressure:	Flowable<Integer> sourceFlowable ....
		
		// TODO 2: map() Schritt: map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
		
		// TODO 3: observeOn(Schedulers.computation()) entkoppelt Downstream-Pipeline von Source-Observable und Map-Schritt
			
		// TODO 4: Subscriber anmelden, diesen ggf. etwas verlangsamen: subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
		
	
		
	}
	
}
