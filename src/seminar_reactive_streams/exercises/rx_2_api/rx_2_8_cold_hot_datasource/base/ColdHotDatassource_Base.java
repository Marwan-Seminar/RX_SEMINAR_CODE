package seminar_reactive_streams.exercises.rx_2_api.rx_2_8_cold_hot_datasource.base;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;

/*
 * 
 * Machen Sie aus einer Hot-Datenquelle eine Hot-Datenquelle und zeigen Sie die Unterschiede
 * 
 * a)	Zeigen Sie, dass bei einer Cold Datenquelle, z.B:  Flowable.range(),
 * 		alle Clients identische Daten erhalten, unabh�ngig vom Zeitpunkt ihrer Anmeldung.
 * 
 * 
 * b)	Machen Sie aus dieser Datenquelle eine Hot Datenquelle, und zeigen Sie,
 * 		dass nun ein sp�ter angemeldeter Subscriber weniger Daten erh�lt, als ein fr�h angemeldeter Subscriber.
 * 		Verwenden Sie folgendes API:
 * 			publish(): Um die eine Cold Datenquelle in eine Hot Datenquelle zu verwandeln
 * 			connect(): Um die Emittierung der Daten zu starten
 * 
 * L�sungsschritte
 * - Datenquelle �Hot� machen: publish()
 * - Ersten Subscriber anmelden
 * - connect() aufrufen, um die Emittierung der Daten zu starten
 * - Eine Zeit lang warten
 * - Zweiten Subscriber anmelden
 * - Im Output sichtbar machen, dass der Zweite Subscriber nicht alle Daten erh�lt, die der erste erhalten hat
 * 
 * Lernziel: Unterschied zwischen einer Cold und einer Hot Datenquelle verstehen
 */
public class ColdHotDatassource_Base {


	public static void main(String[] args) {

		System.out.println("ColdHotDatassource_Base");

		ColdHotDatassource_Base instance = new ColdHotDatassource_Base();
		
		// a)
		instance.a_coldPublisher();

		// b)
		//instance.b_hotPublisher();


		// keep the program alive
		sleep(100000);
	}

	
	/*
	 *  a)	Zeigen Sie, dass bei einer Cold Datenquelle, z.B:  Flowable.range(),
	 * 		alle Clients identische Daten erhalten, unabh�ngig vom Zeitpunkt ihrer Anmeldung.
	 */
	void a_coldPublisher() {

		System.out.println("ColdHotDatassource_Base.a_coldPublisher()");
		
		// Emit  Items 1-15, in 100 Milliseconds Interval
		Flowable<Long> coldSource =  Flowable.intervalRange(1, 15 , 0, 100, TimeUnit.MILLISECONDS);

		// Subscriber early
		coldSource.subscribe(i -> System.out.println("Subscriber early got: " + i));

		// Wait a while before starting the second subscriber
		sleep(1000); 
		System.out.println("Slept 1 second...");

		// TODO: subscriber late: coldSource.subscribe(i -> System.out.println("Subscriber late got: " + i));
	}

	/*
	 *  b)	Machen Sie aus dieser Datenquelle eine Hot Datenquelle, und zeigen Sie,
	 * 		dass nun ein sp�ter angemeldeter Subscriber weniger Daten erh�lt, als ein fr�h angemeldeter Subscriber.
	 * 		Verwenden Sie folgendes API:
	 * 			publish(): Um die eine Cold Datenquelle in eine Hot Datenquelle zu verwandeln
	 * 			connect(): Um die Emittierung der Daten zu starten
	 */
	void b_hotPublisher() {

		System.out.println("ColdHotDatassource_Base.b_hotPublisher()");
		
		// Emit  Items 1-15, in 100 Milliseconds Interval
		Flowable<Long> coldSource =  Flowable.intervalRange(1, 15, 0, 100, TimeUnit.MILLISECONDS);

		// TODO 1: Turn the cold source into a hot source:  coldSource.publish();
		ConnectableFlowable<Long> hotSource = null; 

		// Subscriber early
		hotSource.subscribe(i -> System.out.println("Subscriber early got: " + i));

		// TODO 2: Start the hot source: hotSource.connect();

		// Wait a while before starting the second subscriber
		sleep(1000); 
		System.out.println("Slept 1 second...");

		// Subscriber late
		hotSource.subscribe(i -> System.out.println("Subscriber late got: " + i));
	}



	//////////////////////// HELPER ////////////////////

	static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
