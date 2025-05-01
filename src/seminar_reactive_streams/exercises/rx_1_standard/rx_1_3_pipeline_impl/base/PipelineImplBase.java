package seminar_reactive_streams.exercises.rx_1_standard.rx_1_3_pipeline_impl.base;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicInteger;



/* 
 * 	Uebung 1.3
 * 
 * Implementieren Sie die drei Interfaces aus der Klasse concurrent.Flow:
 * Publisher
 * Subscription
 * Subscriber
 * 
 * Bauen Sie aus diesen eine Pipeline auf, die einen unendlichen Datenfluss verarbeiet.
 * 
 * In dem unten vorgegebenen Rahmen ist die Pipeleine bereits aufgebaut.
 * 
 * Sie hat jedoch folgende zwei Fehler, die Sie beheben muessten:
 * 
 * a) 	In der Methode MySimpleSubscription.request() werden onNext() Aufrufe synchron abgesetzt, das fuehrt zu einem Stack Overflow.
 * 		Dieses Verhalten widerspricht der Regel 3.3: Subscription.request MUST place an upper bound on possible synchronous recursion between Publisher and Subscriber.
 * 		
 * 		Beheben sie dies durch Nutzung eines Executors (das ist ein Thread-Pool)
 * 
 * b) 	In der Methode MySipleSubscriber.onNext(Integer item) wird bei jedem Aufruf nur ein einziges Element uesber den Aufruf request(1) angefordert.
 * 		Dies wiederpricht der Empfehlung aus Regel 1.1, da es ein sogenanntes Stop-And-Wait Protokoll implementiert, was nicht effizient ist.
 * 		
 * 		Beheben Sie dies, indem Sie einen Batch neuer Items bestellen (request(BUFFER_SIZE)).
 * 
 * 
 * 
*/
public class PipelineImplBase {
	
	
	/* 	
	 * Minimalistische Implementierung der jdk Flow Interfaces API.
	 * 
	 * Alle drei Interfaces, Publisher, Subscriber, Subscription sind hier prototypisch implementiert.
	 * Sie folgen jedeoch nicht komplett dem erforderlichen Regelwerk.
	 *  
	 * Diese Implemntierung dient nur zur Demonstration der Bezieuhungen untern den Interfaces, 
	 * die es ermoeglichen eine Pipeline daraus zu bauen.
	 *  
	 *   see: JDK Example in concurrent.Flow Javadoc
	 *   https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html
	 */
	
public static void main(String[] args) {
		
	PipelineImplBase instance = new PipelineImplBase(); 

		// three own classes: Publisher, Subscription, Subscriber
		instance.doSimpleSingleSubscription();
		
		// HACK!!!
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}




	/* 
	 * Implementation of classes for Publisher, Subscriber, Subscription 
	 * Can demonstrate issues that violate rules of the specification, like
	 * - Stack Overflow (Rule 3.3)
	 * - Out of order calls of onNext (Rule 1.3)
	*/
	void doSimpleSingleSubscription() {
		
		// Set up Publisher
		Flow.Publisher<Integer> publisher = new MySimplePublisher();
		
		// Set up Subscriber
		Flow.Subscriber<Integer> subscriber = new MySimpleSubscriber();
		
		// Connect Publisher and Subscriber and trigger publishing
		publisher.subscribe(subscriber);
		
		
		
	}
	
}


/**
 * The Publisher creates the Subscription and passes it to the Subscriber.
 * 
 * Exeption Stack Overflow if no Concurrency used!!
 * 
 * In this example, the method is called out of order!!
 * Therefore my example does not fulfill the specification!!! Details see below. 
 */
class MySimplePublisher implements Flow.Publisher<Integer>{

	
	Flow.Subscriber<? super Integer> subscriber;
	
	// Common ForkJoinPool used here, like in SubmissionPublisher
	// Asynchrony is absolutely required. Otherwise nested calls of request() -> onNext() lead to Stack Overflow 
	Executor executor = ForkJoinPool.commonPool();
	
	@Override
	public void subscribe(Subscriber<? super Integer> subscriber) {
		this.subscriber = subscriber;	
		this.subscriber.onSubscribe(new MySimpleSubscription(subscriber, executor));
	}
	
	
	/*
	 * Typically the Subscription is a static inner class of Publisher
	 */
	static class  MySimpleSubscription implements Flow.Subscription{
		
		Flow.Subscriber<? super Integer> subscriber;
		
		// points to MySimplePublisher.executor
		Executor executor;
		
		// used to generate new data values to be sent.
		int globalData;
		
		
		MySimpleSubscription(Subscriber<? super Integer> subscriber, Executor executor){
			this.subscriber = subscriber;
			this.executor = executor;
		}
 	
		/*
		 * Not standard conforming: Overlapping calls to request() are not serialized!!
		 */
	 	@Override
	 	public void request(long numberOfNewitems) {
	 		
	 		//System.out.println("Subscription Requesting " + numberOfNewitems + " in thread " + Thread.currentThread());
	 		
	 		
	 		
	 		// TODO: Send numberOfNewitems items to the subscriber.
	 		// naive approach below: subscriber.onNext(globalData) in a loop, can lead to stack-overflow
	 		// Resolve this issue through introduction of a thread-pool
	 		// You can use executor declared above.
	 		for(int i = 0; i < numberOfNewitems; ++i) {
 				globalData ++;
 				subscriber.onNext(globalData);
 			}	 		
	 	} 	
	
	 	@Override
	 	public void cancel() {
	 		// TODO Auto-generated method stub
	 		
	 	}
 	
	}	 
}

class MySimpleSubscriber implements Flow.Subscriber<Integer>{

	Flow.Subscription subscription; 
	
	static final int BUFFER_SIZE = 1000;
	
	AtomicInteger receivedCount = new AtomicInteger(0);
	AtomicInteger bufferCount = new AtomicInteger(0);
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(1);
		//this.subscription.request(BUFFER_SIZE);
	}

	
	@Override
	public void onNext(Integer item) {
		if(item % 1000 == 0) {
			System.out.println("Subscriber received:" + item + " in thread " + Thread.currentThread());
		}
		
		receivedCount.incrementAndGet();
		
		// This implements an inefficient "Stop and Wait Protocol", which contradicts rule 1.1
		subscription.request(1);
		// TODO: Find a more efficient approach, requesting a batch of new items
		// for example using BUFFER_SIZE, bufferCount, receivedCount declared above.
		
		
		
		
		/*
		// Logic to request buffer size many items in a batch.
		int currentBufferSize = bufferCount.incrementAndGet();
		if(currentBufferSize == BUFFER_SIZE) {
			// mark buffer as empty again
			bufferCount.set(0);
			// request buffer size many new items
			subscription.request(BUFFER_SIZE);
			
		}
		*/	
	}

	@Override
	public void onError(Throwable throwable) {
		System.err.println("ERROR: "+ throwable);		
	}

	@Override
	public void onComplete() {
		System.out.println("Subscriber: onComplete() called");
		
	}
	
}

