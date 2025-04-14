package seminar_reactive_streams.exercises.rx_1_standard.rx_1_4_jdk_flow.base;

import java.util.concurrent.SubmissionPublisher;

public class JdkFlowSimpleStream_Base {


	public static void main(String[] args) throws InterruptedException {
		
		JdkFlowSimpleStream_Base instance = new JdkFlowSimpleStream_Base();
		
		instance.simpleStream();
		
		
		// TODO: Der SubmissionPublisher laeuft asynchron, daher muss man auf dessen Beendigung "warten", z.B. durch:
		Thread.sleep(10000);
		
	}
	
	
	
	/* 
	 * Benutzen Sie die Klasse SubmissionPublisher als Publisher Implementierung.
	 * 
	 *  */
	void simpleStream(){
		
		// JDK Publisher
		SubmissionPublisher<Integer> publisher = new SubmissionPublisher<Integer>();
		
		// Subscriber (Consumer)
		publisher.consume((item) -> {System.out.println("Subscriber Consuming Item " + item + " in thread " + Thread.currentThread());});
		
		// Items "generieren"
		for(int i = 0; i < 10; ++i) {
			publisher.submit(i);
		}
		
		publisher.close();
	}
}