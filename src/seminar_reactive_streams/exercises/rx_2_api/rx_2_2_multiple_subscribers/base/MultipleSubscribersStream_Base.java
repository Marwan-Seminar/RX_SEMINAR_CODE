package seminar_reactive_streams.exercises.rx_2_api.rx_2_2_multiple_subscribers.base;

import io.reactivex.rxjava3.core.Observable;

/*
 * Verzweigen Sie einen Stream, indem Sie mehrere Subscriber anmelden. 
 * Source ist z.B. Observable.generate(1, 100). 
 * 
 * 
 * a) In welcher Reihenfolge und in welchem Thread laufen die Subscriber ab?
 * 
 * b) Wie laufen die Subscriber, wenn Sie subscribeOn() verwenden?
 * 
 * Loesungshinweis: Um den Thread zu sehen, der einen Subscriber ausfuehrt:
 * 
 * .subscribe(i ->  System.out.println("onNext() " +  i  + " " + Thread.currentThread()));
 * 
 * Lernziel: Nebenlaeufigkeitsverhalten von Streams und die  Wirkung von subscribeOn() kennenlernen
 */
public class MultipleSubscribersStream_Base {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Base");
		
		MultipleSubscribersStream_Base instance = new MultipleSubscribersStream_Base();
		
		// a)		
		instance.a_two_subscribers();
		
		// b)
		instance.b_two_subscribers_multithreaded();
		
	}

	
	/*
	 *   a) In welcher Reihenfolge und in welchem Thread laufen die Subscriber ab?
	 */
	void a_two_subscribers() throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Base.a_two_subscribers()");
		
		Observable<Integer> sourceObservable = Observable.range(1, 100);
					
		// TODO: 1. Subscriber sourceObservable.subscribe....
		
		// TODO: 2. Subscriber
		
				
	}
	
	/*  
	 *  b) Wie laufen die Subscriber, wenn Sie subscribeOn() verwenden?
	 *  
	 *  Loesungshinweis: sourceObservable.subscribeOn(Schedulers.computation())
	 */
	void b_two_subscribers_multithreaded() throws InterruptedException {
		
		System.out.println("MultipleSubscribersStream_Base.b_two_subscribers_multithreaded()");
		
		Observable<Integer> sourceObservable = Observable.range(1, 100);
		
		// TODO:  Turn on Mutltithreading subscribeOn(...);
		
		// TODO: 1. Subscriber
		sourceObservable.subscribe();
		
		// TODO 2. Subscriber
		
		
		
		// HACK: Wait a while until backgrounds threads maybe have completed:
		Thread.sleep(10000);
		
	}
	
}
