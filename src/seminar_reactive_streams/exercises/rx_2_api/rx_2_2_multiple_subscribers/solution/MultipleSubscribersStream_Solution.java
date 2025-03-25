package seminar_reactive_streams.exercises.rx_2_api.rx_2_2_multiple_subscribers.solution;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 * Verzweigen Sie einen Stream, indem Sie mehrere Subscriber anmelden. Source ist z.B. Observable.generate(1, 100). 
 * 
 * 
 * a) In welcher Reihenfolge und in welchem Thread laufen die Subscriber ab?
 * 
 * b) Wie laufen die Subscriber, wenn sie subscribeOn() verwenden?
 * 
 * L�sungshinweis: Um den Thread zu sehen, der einen Subscriber ausf�hrt:
 * 
 * .subscribe(i ->  System.out.println("onNext() " +  i  + " " + Thread.currentThread()));
 * 
 * Lernziel: Nebenl�ufigkeitsverhalten von Streams und die  Wirkung von subscribeOn() kennenlernen
 */
public class MultipleSubscribersStream_Solution {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Solution");
		
		MultipleSubscribersStream_Solution instance = new MultipleSubscribersStream_Solution();
		
		// a)		
		//instance.a_two_subscribers();
		
		// b)
		instance.b_two_subscribers_multithreaded();
		
	}

	
	/*
	 *   a) In welcher Reihenfolge und in welchem Thread laufen die Subscriber ab?
	 */
	void a_two_subscribers() throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Solution.a_two_subscribers()");
		
		Observable<Integer> sourceObservable = Observable.range(1, 100);
		
			
		// 1. Subscriber
		sourceObservable.subscribe(i -> {
			System.out.println("Subscriber 1: " + i + " " + Thread.currentThread());
		});
		

		// 2. Subscriber
		sourceObservable.subscribe(i -> {
			System.out.println("Subscriber 2: " + i + " " + Thread.currentThread());
		});
				
	}
	
	/*  
	 *  b) Wie laufen die Subscriber, wenn sie subscribeOn() verwenden?
	 *  
	 *  L�sungshinweis: sourceObservable.subscribeOn(Schedulers.computation())
	 */
	void b_two_subscribers_multithreaded() throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Solution.b_two_subscribers_multithreaded()");
		
		Observable<Integer> sourceObservable = Observable.range(1, 100);
		
		// Turn on Mutltithreading
		sourceObservable = sourceObservable.subscribeOn(Schedulers.computation());
		
		// 1. Subscriber
		sourceObservable.subscribe(i -> {
			System.out.println("Subscriber 1: " + i + " " + Thread.currentThread());
		});
		

		// 2. Subscriber
		sourceObservable.subscribe(i -> {
			System.out.println("Subscriber 2: " + i + " " + Thread.currentThread());
		});
		
		// HACK: Wait a while until backgrounds threads maybe have completed:
		Thread.sleep(10000);
		
	}
	
}
