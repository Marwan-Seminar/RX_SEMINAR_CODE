package seminar_reactive_streams.exercises.rx_2_api.rx_2_6_genrator_no_backpressure.base;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;


/*
 * Schreiben Sie eine Source fuer einen Stream ohne Back-Pressure.
 * 
 * a)	Schreiben Sie die Stream-Source mit Flowable.create(), der die Zahlen 1 bis 1_000_000 emittiert.
 * 
 * 
 * Programmierbeispiel
 * 
 * Flowable<Integer> source = Flowable.create(emitter -> {
 *			for(int i = 0; i < 200; ++i) {
 *				emitter.onNext(i);
 *			}
 *		},
 * 
 * 
 *  b) 	Zeigen Sie, dass diese Source  keinen Back-Pressure realisiert, 
 * 		selbst wenn Sie einen Parameter fuer die BackpressureStrategy uebergeben. 
 * 
 * Lernziel: Stream Source ohne Back-Pressure erzeugen koennen
 */
public class GenaratorNoBackpressure_Base {

	public static void main(String[] args) {
		
		System.out.println("GenaratorNoBackpressure_Base");
		
		GenaratorNoBackpressure_Base instance = new GenaratorNoBackpressure_Base();
		
		// a)
		instance.a_createStreamSource();
		
		// b) 
		//instance.b_noBackpressureAvailable();
		
		// keep the program running
		sleep(100000);
	}
	
	
	/*
	 * a)	Schreiben Sie die Stream-Source mit Flowable.create(), der die Zahlen 1 bis 1_000_000 emittiert.
	 */
	void a_createStreamSource() {
		
		System.out.println("GenaratorNoBackpressure_Base.a_createStreamSource()");
		
		// So sieht das API aus:
		//  <T> Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode
		// public interface FlowableOnSubscribe<T> { void subscribe( FlowableEmitter<T> emitter)}
	
		// TODO uebergeben Sie ein Lambda, das in einer Schleife auf dem emitter die Methode
		// onNext() aufruft, um Daten in den Stream zu pushen. 
		Flowable<Integer> source = null; // Flowable.create(emitter -> { ...emitter.onNExt() ...
		
		// Einen Subscriber anmelden
		source.subscribe(nextElement -> {
			System.out.println("Subscriber received onNext() Element: " +  nextElement);
		});
	}
	
	
	/*
	 *  b) 	Zeigen Sie, dass diese Source  keinen Back-Pressure realisiert, 
	 *  	selbst wenn Sie einen Parameter furr die BackpressureStrategy uebergeben. 
	 *  
	 *  Im Shell-Output wird sichtbar, dass der Erzeuger und der Subscriber immer
	 *  weiter auseinander laufen.
	 */
	void b_noBackpressureAvailable() {
		
		System.out.println("GenaratorNoBackpressure_Solution.b_noBackpressureAvailable()");
		 
		Flowable<Integer> source = Flowable.create(emitter -> {
			for(int newElement = 0; newElement < 1_000_000; ++newElement) {
				emitter.onNext(newElement);
				System.out.println("Emitter created New Element:" +  newElement);
			}
		},
		// BackpressureStrategy.DROP: alles ab 128 geht verloren
		// BackpressureStrategy.BUFFER f�hrt zu einem unendlichen Buffer		
		BackpressureStrategy.BUFFER); 
		
		// TODO 1: Entkoppung der Downstream-Pipeline: observeOn(Schedulers.computation());
		

		source.subscribe(nextElement -> {
			System.out.println("Subscriber received onNext() Element: " +  nextElement);
			// TODO 2: 	verlangsamen Sie den Subscriber,
			// 			z.B. durch Aufruf von cpuIntensiveLoop(1),
			//			so dass man im Output sieht, dass die StreamSource
			// 			und der Subscriber immer weiter auseinanderlaufen
		});
	}
	
	
	
/////////////// HELPER /////////////////////
	
	// runs roughly millis Milliseconds.
	static void cpuIntensiveLoop(int millis) {

		// long start = System.currentTimeMillis();
		for (int i = 0; i < millis; i++) {
			for (long l = 0; l < 4000000L; ++l) {

			}
		}
	}
	
	static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
