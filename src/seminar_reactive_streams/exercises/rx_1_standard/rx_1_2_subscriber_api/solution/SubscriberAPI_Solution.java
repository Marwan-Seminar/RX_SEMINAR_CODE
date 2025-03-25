package seminar_reactive_streams.exercises.rx_1_standard.rx_1_2_subscriber_api.solution;

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
public class SubscriberAPI_Solution {
	
	public static void main(String[] args) {
		
		SubscriberAPI_Solution instance = new SubscriberAPI_Solution();
		
		instance.runMySubsriber();
	}
	

	private void runMySubsriber() {
	
		
		Flowable<Integer> source = Flowable.range(1,  100);
		
		//source.subscribe(System.out::println);
		
		source.subscribe(new MySimpleSubscriber());
	}
	

}


class MySimpleSubscriber implements Subscriber<Integer> {

	// Member Variable, um sich die Subscription zu merken.
	Subscription subscription;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		
		System.out.println("MySimpleSubscriber.onSubscribe(): " + subscription.getClass());
		
		// TODO Subscription merken, so dass Elemnte angefordert werden k�nnen
		this.subscription = subscription;
		
		// TODO Das erste Element anfordern
		subscription.request(1);
		
	}

	@Override
	public void onNext(Integer item) {
		
		System.out.println("MySimpleSubscriber.onNext(): " + item);
		
		// TODO Ein neues Element bei der Subscription bestellen ("Back-Pressure")
		subscription.request(1);
		
		// Dieses Vorgehen funktioniert zwar, ist aber nicht effizient. 
		// Ein realer Subscriber w�rde einen Batch von Elementen bestellen. 
				
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
