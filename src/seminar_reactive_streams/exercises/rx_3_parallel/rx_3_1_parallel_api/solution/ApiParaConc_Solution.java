package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_1_parallel_api.solution;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
public class ApiParaConc_Solution {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("ApiParaConc_Solution");
		
		ApiParaConc_Solution instance = new ApiParaConc_Solution();

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
		
		System.out.println("ApiParaConc_Solution.a_sequentialStream()");
		
		// SOURCE
		Observable.range(0, 1000)
		
		// TODO CONCURRENCY: subscribeOn() decouples whole Stream from main thread
		//.subscribeOn(Schedulers.computation())
		
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
		
		
		// HACK (required if subscribeOn() is used): Wait a  until backgrounds threads maybe have completed:
		sleep(10000);
	}
	
	
	/*
	 * b) subscribeOn()-Paralleli�t:
	 * Melden Sie einen zweiten Subscriber an dem
	 * Stream an, und nutzen Sie subscribeOn() um beide Subscriber parallel zu
	 * bedienen. Zeigen Sie die Parallelit�t durch Shell-Ausgaben.
	 */	 	
	void b_subscribeOnParallelism() throws InterruptedException {
		
		System.out.println("ApiParaConc_Solution.b_subscribeOnParallelism()");
		
		Observable<Integer> sourceParallel =
				Observable.range(0, 1000)
				.subscribeOn(Schedulers.computation());
		
		// Subscriber 1
		sourceParallel
			.subscribe(i -> {
				System.out.println("SUBSCRIBE 1:  " + i  + " " + Thread.currentThread());
			});

		// Subscriber 2
		sourceParallel
			.subscribe(i -> {
				System.out.println("SUBSCRIBE 2:  " + i  + " " + Thread.currentThread());
			});
		
		
		// HACK: Wait a  until backgrounds threads maybe have completed:
		sleep(10000);
		
	}
	
	/*
	 * c) observeOn()- Nebenl�ufigkeit:
	 * Entkoppeln Sie die beiden map() Stufen durch
	 * Einsatz von observeOn() voneinander. Zeigen Sie durch Shell-Ausgaben, dass
	 * die Stufen asynchron zueinander laufen.
	 */
	void c_obsereveOnConcurrency(){
		
		System.out.println("ApiParaConc_Solution.c_obsereveOnConcurrency()");
		
		// SOURCE
		Observable.range(0, 1000)		
		// MAP 1
		.map(i -> {
			System.out.println("MAP 1: " + i + " " + Thread.currentThread());
			return i;})
		
		// CONCRURRENCY: observeOn() decoupels downstream-pipeleine from upstream-pipeline
		.observeOn(Schedulers.computation())
		
		// MAP 2
		.map (i -> {
			System.out.println("MAP 2: " + i + " " + Thread.currentThread());
			return i;})
		// SUBSCRIBE
		.subscribe(i -> {
			System.out.println("SUBSCRIBE: " + i  + " " + Thread.currentThread());
		});
						
		
		// HACK: Wait a while until backgrounds threads maybe have completed:
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
