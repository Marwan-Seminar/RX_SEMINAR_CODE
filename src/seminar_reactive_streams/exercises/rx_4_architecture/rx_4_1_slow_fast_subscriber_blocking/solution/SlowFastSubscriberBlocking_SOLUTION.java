package seminar_reactive_streams.exercises.rx_4_architecture.rx_4_1_slow_fast_subscriber_blocking.solution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * Aufgabe zur Skalierbarkeit.
 * 
 * Langsame Subscriber sollten schnelle Subscriber nicht behindern, wenn beide bei dem selben Publisher subskribiert sind.
 * Wie stellt man das sicher?
 * 
 * 1. PROBLEM:
 * 	 	Zeigen Sie, dass es passieren kann, dass ein potentiell schneller Subscriber nicht l�uft, 
 * 		weil viele "langsame" Subscriber Threads nicht freigeben. 
 * 
 * 		"langsam" bedeutet hier, dass in onNext() eine lang laufende Aufgabe bearbeitet wird.
 * 
 * 2. L�SUNG:
 * 		Finden Sie eine L�sung, die sicherstellt, dass der schnelle Subscriber ungehindert laufen kann, auch wenn mehrere langsame
 * 		Subscriber beim Publisher angemeldet sind.
 */
public class SlowFastSubscriberBlocking_SOLUTION {

	public static void main(String[] args) {
	
		SlowFastSubscriberBlocking_SOLUTION instance = new SlowFastSubscriberBlocking_SOLUTION();
		
		
		// 1: PROBLEM, langsame Subscriber hindern schnellen Subscriber am Laufen 
		//instance.slowSubscribersBlockingFastSubscriber();
	
		// 2. L�SUNG
		instance.asynchSubscriberExplicitBackpressure();
		
		// keep the program running
		sleep(100000);
	}
	
	/*
	 * 1. PROBLEM
	 * 
	 * Hier sieht man, wie mehrere "langsame" Subscriber einen schnellen Subscriber blockieren.
	 */
	void slowSubscribersBlockingFastSubscriber() {

		System.out.println("SlowFastSubscriberBlocking_SOLUTION.slowSubscribersBlockingFastSubscriber()");
		
		Observable<Integer> source = Observable.range(1, 500);
		//Flowable<Integer> source = Flowable.range(1, 500);
		
		// mit subscribeOn() wechseln sich schnelle und langsame Subscriber
		// ab, aber nur solange es mehr Threads im Pool gibt, als Subscriber,
		// denn jeder Subscriber hat seinen eigenen Thread. Das skaliert also nicht.
		source = source.subscribeOn(Schedulers.computation());

		// Debug output only
		source = source.map(i -> {
			System.out.println("MAP " + i + " running in " + Thread.currentThread());
			cpuIntensiveLoop(1);
			return i;
		});

		// observeOn(): F�r jeden Subscriber gibt es eine Queue der L�nge 128
		source = source.observeOn(Schedulers.computation());

		// Viele langsame Subscriber:
		// Wenn es mit subscribeOn() mehr dieser langsamen Subscriber gibt als Threads im Pool,
		// kommt der schnelle Subscriber erst am Schluss zum Zug!
		for (int subscriberIdx = 0; subscriberIdx < 8; subscriberIdx++) {

			final int subscriberID = subscriberIdx;

			// Slow subscriber
			source.subscribe(
					element -> {
						System.out.println("SLOW subscriber " + subscriberID + " received: " + element + " " + Thread.currentThread());
						cpuIntensiveLoop(10);
					});
		}

		// Fast Subscriber
		source.subscribe(element -> {
			System.out.println("FAST subscriber received: " + element + " " + Thread.currentThread());
			cpuIntensiveLoop(2);
		});

	}

	/*
	 * 2. L�USUNG
	 * 
	 * hat folgende Anteile
	 * - Subscriber in eigener Klasse (BackpressureSubscriber),
	 *  	bei der onNext() sofort zur�ckkehrt, und der langlaufende Code in einen anderen Thread ausgelagert wird.
	 *  
	 * - Der Subscriber verwendet Backpressure:
	 *  	Er bestellt immer nur neue Elemente, wenn der langlaufende Code f�r ein vorhergehendes Element	abgearbeitet ist.
	 *  
	 * - Flowable um Backpressure zu erm�glichen  
	 */
	void asynchSubscriberExplicitBackpressure() {

		System.out.println("SlowFastSubscriberBlocking_SOLUTION.asynchSubscriberExplicitBackpressure()");
		
		Flowable<Integer> source = Flowable.range(1, 500);
		
		// subscribeOn() ist in diesem Scenario nicht zwingend erforderlich, aber m�glich und sinnvoll
		// source = source.subscribeOn(Schedulers.computation());

		// Debug output only
		source = source.map(i -> {
			System.out.println("MAP " + i + " running in " + Thread.currentThread());
			cpuIntensiveLoop(1);
			return i;
		});

		// observeOn(): Auch observeOn() ist nicht zwingend erforderlich, aber m�glich und sinnvoll.
		// 
		// Mit observeOn() gibt es f�r jeden Subscriber einen Buffer der Tiefe 128.
		// observeOn() entkoppelt Subscriber und Source:
		//
		//source = source.observeOn(Schedulers.computation());

		// Viele langsame Subscriber:
		for (int subscriberIdx = 0; subscriberIdx < 8; subscriberIdx++) {

			final int subscriberID = subscriberIdx;

			// Slow subscriber
			source.subscribe( new BackpressureSubscriber(subscriberID));

		}

		// kurz warten, bevor der FAST subscriber gestartet wird.
		sleep(1000);
	
		// Fast Subscriber
		source.subscribe(element -> {
			System.out.println("FAST subscriber received: " + element + " " + Thread.currentThread());
			cpuIntensiveLoop(2);
		});

	}
	
	/*
	 * Teil der L�sung: Subscriber Klasse f�r expliziten Backpressure im Subscriber
	 * 
	 * Diese Subscriber Implementierung steuert zwei Aspekte zur L�sung bei:
	 * 
	 * 1.  Um sicherzustellen, dass die Regel 2.2 der Reactive-Streams Spezifikation nicht verletzt wird,
	 * 	kehrt der Aufruf von onNext() sofort zur�ck, und l�nger laufende Aufgaben in einen andern asychronen Aufruf augelagert.
	 * 
	 * 2. Expliziter Backpressure:
	 * 	Erst wenn die CPU-intensive Arbeit erledigt ist, wird ein neues Element angefordert, mit dem Aufruf von
	 * 	Subscription.request().
	 */
	static class BackpressureSubscriber implements Subscriber<Integer> {

		Subscription mySubscription;
		int subscriberID;
		

		// ThreadPool for async execution: Size can be changed to optimize CPU utilization
		static ExecutorService pool = Executors.newFixedThreadPool(5);

		BackpressureSubscriber(int id) {
			this.subscriberID = id;
		}

		@Override
		public void onSubscribe(Subscription subscritpion) {
			this.mySubscription = subscritpion;
			mySubscription.request(1);
		}

		@Override
		public void onNext(Integer element) {

			//System.out.println("SLOW subscriber " + subscriberID + " received: " + element + " " + Thread.currentThread());

			// fire and forget
			pool.submit(() -> {

				System.out.println("SLOW subscriber " + subscriberID + " processing: " + element + " " + Thread.currentThread());

				cpuIntensiveLoop(10);

				// !!! EXPLICIT BACKPRESSURE 
				mySubscription.request(1);
				// Das Source-Observable verwendet diesen Thread weiter f�r den n�chsten onNext() call auf diesem Subscriber. 
			});

		}

		@Override
		public void onError(Throwable t) {
			System.out.println("SLOW subscriber " + subscriberID + " onError");

		}

		@Override
		public void onComplete() {
			System.out.println("SLOW subscriber " + subscriberID + " OnComplete");
		}

	}
	
	///////////// HELPER ///////////
	// runs roughly millis Milliseconds, absolutely not precise
	static void cpuIntensiveLoop(int millis) {
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
