package seminar_reactive_streams.exercises.rx_1_standard.rx_1_3_pipeline_impl.solution;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.atomic.AtomicInteger;


/* 
 * Shows Minimalsitic Implementation of jdk Flow Interfaces API for Slide
 *  
 * 
 *  Alle drei Interfaces, Publisher, Subscriber, Subscription sind hier prototypisch implementiert.
 *  Sie folgen jedeoch nicht komplett dem erforderlichen Regelwerk.
 *  
 *  Diese Implemntierung dient nur zur Demonstration der Bezieuhungen unter den Interfaces, 
 *  die es ermoeglichen eine Pipeline daraus zu bauen.
 *  
 *  see: JDK Example in concurrent.Flow Javadoc
 *  https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html
 * 
*/
public class PipelineImplSolution {

public static void main(String[] args) {
		
	PipelineImplSolution instance = new PipelineImplSolution(); 

		// three own classes: Publisher, Subscription, Subscriber TODO StackOverflwo
		instance.doSimpleSingleSubscription();
		
		// HACK!!! 
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}




	/* Own classes for Publisher, Subscriber, Subscription 
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



/*
 * The Publisher creates the Subscription and passes it to the Subscriber.
 * 
 * Exception Stack Overflow if no Concurrency used!
 * 
 * This example code does not fulfill the specification!!! Details see below. 
 */
class MySimplePublisher implements Flow.Publisher<Integer>{

	/*
	 * IMPORTANT Note: This implementation is not fully compliant with the rules of the standard. 
	 *  
	 * Rule 3 from the ReactiveSreams Specification states:
	 * onSubscribe, onNext, onError and onComplete signaled to a Subscriber MUST be signaled serially.
	 * 
	 * Therefore  the Flow Javadoc states: 
	 * Subscriber method invocations for a given Flow.Subscription are strictly ordered,
	 * there is no need for these methods to use locks or volatiles.
	 * 
	 * See BufferedSubscription: This seems to guarantee that there is only one single active task!!!
	 * But BufferedSubscription is only used by SubmissionPublisher and not here in my example.
	 * 
	 * In this example, the subscriber methods may be called out of order, if request-calls overlap.
	 * Therefore my example does not fulfill the specification!!! 
	 * 
	 */
	
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
	 		
	 		// all onNext() invocations happen serially within one Task to fulfill Rule 1.3
	 		executor.execute(() ->{
 				for(int i = 0; i < numberOfNewitems; ++i) {
 					globalData ++;
 					subscriber.onNext(globalData);
 				}
	 		});
	 		
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
		this.subscription.request(BUFFER_SIZE);
	}

	
	@Override
	public void onNext(Integer item) {
		if(item% 1000 == 0) {
			System.out.println("Subscriber received:" + item + " in thread " + Thread.currentThread());
		}
		
		receivedCount.incrementAndGet();
		
		int currentBufferSize = bufferCount.incrementAndGet();
		
		// Logic to request buffer size many items in a batch.
		if(currentBufferSize == BUFFER_SIZE) {
			// mark buffer as empty again
			bufferCount.set(0);
			// request buffer size many new items
			subscription.request(BUFFER_SIZE);
			
		}	
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

