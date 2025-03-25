package seminar_reactive_streams.exercises.rx_1_standard.rx_1_4_jdk_flow.solution;

import java.util.concurrent.SubmissionPublisher;

public class JdkFlowSimpleStream_Solution {


	public static void main(String[] args) throws InterruptedException {
		
		JdkFlowSimpleStream_Solution instance = new JdkFlowSimpleStream_Solution();
		
		instance.simpleStream();
		
		//instance.simpleStreamSeveralSubscribers();
		
		Thread.sleep(10000);
		
	}
	
	
	
	/* Example 1: Using the SubmissionPublisher class, which contains predefined Subscriber and Subscription
	 * shows, that SubmissionPublisher uses ForkJoinPool to send items to Subscribers. 
	*/
	void simpleStream(){
		
		// JDK Publisher
		SubmissionPublisher<Integer> publisher = new SubmissionPublisher<Integer>();
		
		// 1. Subscriber (Consumer)
		publisher.consume((item) -> {System.out.println("Subscriber 1 Consuming Item " + item + " in thread " + Thread.currentThread());});
		
		
		for(int i = 0; i < 10; ++i) {
			publisher.submit(i);
		}
		
		publisher.close();
	}
	
	/* Example 2: Using the SubmissionPublisher, which contains predefined Subscriber and Subscription
	 * shows, that SubmissionPublisher uses ForkJoinPool to send items to Subscribers. 
	 * If I register one Subscriber only, it is serviced within one single Thread
	 * If I register two subscribers, they are serviced in two threads. Subscriber x is serviced by the same thread in each call (? must?).
	 * 
	*/
	void simpleStreamSeveralSubscribers(){
		
		// JDK Publisher
		SubmissionPublisher<Integer> publisher = new SubmissionPublisher<Integer>();
		
		// 1. Subscriber (Consumer)
		publisher.consume((item) -> {System.out.println("Subscriber 1 Consuming Item " + item + " in thread " + Thread.currentThread());});
		
		// 2. Subscriber (Consumer)
		publisher.consume((item) -> {System.out.println("Subscriber 2 Consuming Item " + item + " in thread " + Thread.currentThread());});
		
		for(int i = 0; i < 10; ++i) {
			publisher.submit(i);
		}
		
		publisher.close();
	}

}