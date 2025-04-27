package seminar_reactive_streams.exercises.rx_2_api.rx_2_6_genrator_no_backpressure.solution;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * Schreiben Sie eine Source fuer einen Stream ohne Backpressure.
 * 
 * a)	Schreiben Sie eine Stream-Source mit Flowable.create(), die Zahlen 1 bis 1_000_000 emittiert.
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
 * 
 */
public class GenaratorNoBackpressure_Solution {

	public static void main(String[] args) {
		
		System.out.println("GenaratorNoBackpressure_Solution");
		
		GenaratorNoBackpressure_Solution instance = new GenaratorNoBackpressure_Solution();
		
		// a)
		//instance.a_createStreamSource();
		
		// b) 
		instance.b_noBackpressureAvailable();
		
		// keep the program running
		sleep(100000);
	}
	
	
	/*
	 * a)	Schreiben Sie die Stream-Source mit Flowable.create(), der die Zahlen 1 bis 1_000_000 emittiert.
	 */
	void a_createStreamSource() {
		
		System.out.println("GenaratorNoBackpressure_Solution.a_createStreamSource()");
		
		// So sieht das API aus:
		//  <T> Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode
		// public interface FlowableOnSubscribe<T> { void subscribe( FlowableEmitter<T> emitter)}
	
		// Anwendung: 
		Flowable<Integer> source = Flowable.create(emitter -> {
			for(int newElement = 0; newElement < 1_000_000; ++newElement) {
				emitter.onNext(newElement);
				System.out.println("Emitter created New Element:" +  newElement);
			}
		},
		// BackpressureStrategy.DROP: alles ab 128 geht verloren
		// BackpressureStrategy.BUFFER fuerht zu einem unendlichen Buffer		
		BackpressureStrategy.BUFFER); 
						
		source.subscribe(nextElement -> {
			System.out.println("Subscriber received onNext() Element: " +  nextElement);
		});
	}
	
	
	/*
	 *  b) 	Zeigen Sie, dass diese Source  keinen Back-Pressure realisiert, 
	 *  	selbst wenn Sie einen Parameter fuer die BackpressureStrategy uebergeben. 
	 *  
	 *  Im Shell-Output wird sichtbar, dass der Erzeuger und der Subscriber immer
	 *  weiter auseinander laufen, da hier der Subscriber verlangsamt ist. 
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
		// BackpressureStrategy.BUFFER fuehrt zu einem unendlichen Buffer		
		BackpressureStrategy.BUFFER); 
		
		// Entkoppung der Downstream-Pipeline
		source = source.observeOn(Schedulers.computation());
		
		// Langsamer Subscriber: 
		source.subscribe(nextElement -> {
			System.out.println("Subscriber received onNext() Element: " +  nextElement);
			cpuIntensiveLoop(1);
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
