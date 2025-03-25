package seminar_reactive_streams.exercises.rx_1_standard.rx_1_2_subscriber_api.base;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.Flowable;

/*
 * RX 1.2 Subscriber API und Lifecycle
 * org.reactivestreams.Subscriber implementieren
 * 
 * Programmieren Sie einen Subscriber.
 * Implementieren Sie daf�r das Interface org.reactivestreams.Subscriber.
 * 
 * a) Subcribieren Sie Ihren Subscriber an ein RxJava Flowable.range(1,100)
 * 
 * b) Zeigen Sie durch Shell-Ausgaben, wann welche Methoden aufgerufen werden. 
 * 
 * HINWEIS: 
 * 	- Sie m�ssen sich die Subscription �merken�, die im onSubscribe() Aufruf �bergeben wird
 *	- Sie m�ssen Subscription.request() aufrufen, damit Daten ausgesendet werden.
 *
 * Lernziel: Subscriber API und Lifecycle kennenlernen
 *
 */
public class SubscriberAPI_Base {
	
	public static void main(String[] args) {
		
		SubscriberAPI_Base instance = new SubscriberAPI_Base();
		
		instance.runMySubsriber();
	}
	

	private void runMySubsriber() {
	
		// Flowable source ist eine Stream-Source. Sie emittiert die Zahlen 1-100.
		Flowable<Integer> source = Flowable.range(1,  100);
		
		// Subscriber Instanz bei der Source anmelden.
		source.subscribe(new MySimpleSubscriber());
	}
	

}

/*
 * Dies ist der Rahmen f�r die Subscriber Implementierung.
 * 
 * Die Logik innerhalb der Methoden onSubscribe() und onNext() muss noch implementiert werden,
 * an denjenigen Stellen, an denen TODO steht. 
 */
class MySimpleSubscriber implements Subscriber<Integer> {

	// Member Variable, um sich die Subscription zu merken.
	Subscription subscription;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		
		System.out.println("MySimpleSubscriber.onSubscribe(): " + subscription.getClass());
		
		// TODO: Subscription merken, so dass Elemnte angefordert werden k�nnen
		
		
		// TODO: Das erste Element anfordern: Subscription.request(1)
		
	}

	@Override
	public void onNext(Integer item) {
		
		System.out.println("MySimpleSubscriber.onNext(): " + item);
		
		// TODO: Ein neues Element bei der Subscription bestellen ("Back-Pressure")
					
	}

	@Override
	public void onError(Throwable t) {
		System.out.println("MySimpleSubscriber.onError()");
	}

	@Override
	public void onComplete() {
		System.out.println("MySimpleSubscriber.onComplete() called");
	}
	
}
