package seminar_reactive_streams.exercises.rx_4_architecture.rx_4_1_slow_fast_subscriber_blocking.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;


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
public class SlowFastSubscriberBlocking_BASE {

	public static void main(String[] args) {
	
		SlowFastSubscriberBlocking_BASE instance = new SlowFastSubscriberBlocking_BASE();
		
		
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
	 * Zeigen Sie, dass mehrere langlaufende CPU-intensive Subscriber 
	 * einen andern "schnellen" Subscriber blocieren k�nnen.
	 * 
	 * Ergebnis: Shell-Output der zeigt, dass der "schnelle" erst dann l�uft, wenn die "langsamen" fertig sind
	 */
	void slowSubscribersBlockingFastSubscriber() {

		// Eine Source emittiert Objekte
		Observable<Integer> source = Observable.range(1, 500);
		

		// Debug output only
		source = source.map(i -> {
			System.out.println("MAP " + i + " running in " + Thread.currentThread());
			cpuIntensiveLoop(1);
			return i;
		});


		// TODO 1 subskribieren Sie mehrere Subscriber an der Source (mehr als Ihr Rechner CPUs hat), 
		// und sorgen Sie daf�r, dass diese CPU-intensiven Code in onNext() ausf�hren
		
		// Hinweis:
		/* 
			source.subscribe( i -> {
				// etwas das CPU intensiv ist, z.B: 
				// cpuIntensiveLoop(10);
				// Schreiben Sie einen Debug-Output auf die Shell, um zu zeigen, was passiert
			});
		*/	
		
		// eine kleine Verz�gerung...
		sleep(100);
		
		
		// TODO 2: melden Sie danach einen schnellen Subscriber an, der nur wenig CPU Zeit in onNext() verbringt
		
		// Hinweis:
		/*
			source.subscribe(element -> {
				// Debug Output
			});
		*/	

	}

	/*
	 * 2. L�USUNG
	 * 
	 * Finden Sie eine L�ung f�r das Problem. 
	 * 
	 * Hinweise:
	 *  
	 * - Implementieren Sie den Subscriber in einer eigenen Klasse,
	 *   bei der onNext() sofort zur�ckkehrt, und der langlaufende Code in einen anderen Thread ausgelagert wird.
	 *  
	 * - Der Subscriber verwendet Backpressure:
	 *   Er bestellt immer nur neue Elemente, wenn der langlaufende Code f�r ein vorhergehendes Element	abgearbeitet ist.
	 *  
	 * - Nutzen Sie ein Flowable als Source, um Backpressure zu erm�glichen  
	 */
	void asynchSubscriberExplicitBackpressure() {

		System.out.println("SlowFastSubscriberBlocking.asynchSubscriberExplicitBackpressure()");
		
		Flowable<Integer> source = null;
		
		// TODO 1: Erzeugen Sie Daten aus einem Flowable
		// source = Flowable.range(1, 500);
		
		// subscribeOn() ist in diesem Scenario nicht zwingend erforderlich, aber m�glich und sinnvoll
		// source = source.subscribeOn(Schedulers.computation());

		// Debug output only
		source = source.map(i -> {
			System.out.println("MAP " + i + " running in " + Thread.currentThread());
			cpuIntensiveLoop(1);
			return i;
		});

		// observeOn(): Auch observeOn() ist nicht zwingend erforderlich, aber m�glich und sinnvoll.
		//source = source.observeOn(Schedulers.computation());

		
		// Viele langsame Subscriber:
		for (int subscriberIdx = 0; subscriberIdx < 8; subscriberIdx++) {
			// TODO 2  instanziieren Sie hier ihre eigene Subscriber Klasse und melden die Instanzen an der Source an
			// source.subscribe( new BackpressureSubscriber(subscriberID));

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
	 * TODO 3: Schreiben Sie Ihre eigene Subscriber Klasse, mit folgenden Eigenschafen 
	 * 
	 * 1. Der Aufruf von onNext() kehrt sofort zur�ck, und l�nger laufende Aufgaben werden in einen  asychronen Aufruf augelagert.
	 * 
	 * 2. Expliziter Backpressure: Erst wenn die CPU-intensive Arbeit erledigt ist, wird ein neues Element angefordert, mit dem Aufruf von
	 * 	Subscription.request().
	 */
	static class BackpressureSubscriber implements Subscriber<Integer> {

		// Die Subscription ist die Verbindung zum Publisher bzw. zum Flowable
		Subscription mySubscription;
		
		int subscriberID;
		

		// ThreadPool for async execution: Size can be changed to optimize CPU utilization
		static ExecutorService pool = Executors.newFixedThreadPool(1);

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

			System.out.println("SLOW BackpressureSubscriber " + subscriberID + " received: " + element + " " + Thread.currentThread());

			// TODO 4: Hier die Asynchronit�t einbauen,
			// 	CPU-Intensiven Call ausf�hren: z.B. cpuIntensiveLoop(10);
			// 	und neues Element von der Subscription anfordern: mySubscription.request(1);
			pool.submit(() -> {
				//... 
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
