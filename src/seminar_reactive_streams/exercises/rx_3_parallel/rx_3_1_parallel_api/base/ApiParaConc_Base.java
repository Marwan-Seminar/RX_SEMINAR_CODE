package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_1_parallel_api.base;

import io.reactivex.rxjava3.core.Observable;

/*
 * 
 * 
 *  Programmieren Sie einen endlichen Datenfluss mit vier Stream-Stufen:
 *  Ein Flowable oder Observable sendet Daten aus, zwei map() Stages verarbeiten die Daten,
 *  ein Subscriber empf�ngt die Daten.  
 *  	
 *  Zum Beispiel
 *  	- Flowable.range()
 *  	-	map(i -> i)
 *  	-	map(i -> i )
 *  	-	subsribe(println)
 *  
 * a) 	Sequentialit�t: Zeigen Sie, dass ein Stream zun�chst sequentiell im Main Thread abl�uft. 
 * 		Ver�ndern Sie dieses Verhalten durch subscribeOn().
 * 
 * b) 	subscribeOn()-Paralleli�t: Melden Sie einen zweiten Subscriber an dem Stream an,
 * 		und nutzen Sie subscribeOn() um beide Subscriber parallel zu bedienen.
 * 	 	Zeigen Sie die Parallelit�t durch Shell-Ausgaben.
 * 
 * c) 	observeOn()- Nebenl�ufigkeit: Entkoppeln Sie die beiden map() Stufen durch Einsatz von observeOn() voneinander.
 * 	 	Zeigen Sie durch Shell-Ausgaben, dass die Stufen asynchron zueinander laufen.
 * 
 * Lernziel: Grundlegende API f�r Parallelit�t und Nebenl�ufigkeit verstehen
 * 
 *  
 */
public class ApiParaConc_Base {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("ApiParaConc_Base");
		
		ApiParaConc_Base instance = new ApiParaConc_Base();

		// a)
		instance.a_sequentialStream();
		
		// b)
		//instance.b_subscribeOnParallelism();
		
		// c
		//instance.c_obsereveOnConcurrency();
		
	}

	/*
	 * a) Sequentialit�t:
	 * Zeigen Sie, dass ein Stream zun�chst sequentiell im Main
	 * Thread abl�uft. Ver�ndern Sie dieses Verhalten durch subscribeOn().
	 */
	void a_sequentialStream(){
		
		System.out.println("ApiParaConc_Base.a_sequentialStream()");
		
		// SOURCE
		Observable.range(0, 1000)
		
		// TODO CONCURRENCY: subscribeOn() decouples whole Stream from main thread
		
		// MAP 1
		.map(i -> {
			System.out.println("MAP 1: " + i + " " + Thread.currentThread());
			return i;})
		// MAP 2
		.map (i -> {
			System.out.println("MAP 2: " + i + " " + Thread.currentThread());
			return i;})
		// SUBSCRIBE
		.subscribe(i -> {
			System.out.println("SUBSCRIBE: " + i  + " " + Thread.currentThread());
		});
		
		
		//  Wait a  until backgrounds threads maybe have completed: (required if subscribeOn() is used)
		sleep(10000);
	}
	
	
	/*
	 * b) subscribeOn()-Paralleli�t:
	 * Melden Sie einen zweiten Subscriber an dem
	 * Stream an, und nutzen Sie subscribeOn() um beide Subscriber parallel zu
	 * bedienen. Zeigen Sie die Parallelit�t durch Shell-Ausgaben.
	 */	 	
	void b_subscribeOnParallelism() throws InterruptedException {
		
		System.out.println("ApiParaConc_Base.b_subscribeOnParallelism()");
		
		Observable<Integer> sourceParallel =
				Observable.range(0, 1000);
		
		// TODO Stream parallelisieren: subscribeOn() / Schedulers.computation()
		
		// TODO Subscriber 1 anmelden

		// TODO Subscriber 2 anmelden
		
		
		// Wait a  until backgrounds threads maybe have completed:
		sleep(10000);
		
	}
	
	/*
	 * c) observeOn()- Nebenl�ufigkeit:
	 * Entkoppeln Sie die beiden map() Stufen durch
	 * Einsatz von observeOn() voneinander. Zeigen Sie durch Shell-Ausgaben, dass
	 * die Stufen asynchron zueinander laufen.
	 */
	void c_obsereveOnConcurrency(){
		
		System.out.println("ApiParaConc_Base.c_obsereveOnConcurrency()");
		
		// SOURCE
		Observable.range(0, 1000)		
		// MAP 1
		.map(i -> {
			System.out.println("MAP 1: " + i + " " + Thread.currentThread());
			return i;})
		
		// TODO CONCRURRENCY: observeOn() decouples downstream-pipeline from upstream-pipeline
	
		
		// MAP 2
		.map (i -> {
			System.out.println("MAP 2: " + i + " " + Thread.currentThread());
			return i;})
		// SUBSCRIBE
		.subscribe(i -> {
			System.out.println("SUBSCRIBE: " + i  + " " + Thread.currentThread());
		});
						
		
		// Wait a while until backgrounds threads maybe have completed:
		sleep(10000);
	}
	


	
/////////////// HELPER /////////////////////
	

	static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

}
