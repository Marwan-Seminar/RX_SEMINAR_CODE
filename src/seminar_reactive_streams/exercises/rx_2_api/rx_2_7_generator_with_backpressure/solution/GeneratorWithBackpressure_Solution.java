package seminar_reactive_streams.exercises.rx_2_api.rx_2_7_generator_with_backpressure.solution;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * RX 2.6 Generator mit Back-Pressure bauen
 * 
 * a) Erzeugen Sie eine Stream Source unter Verwendung von Flowable.generate() 
 * 
 * Bauen Sie darauf einen trivialen Stream auf: 
 *     	.map (i->i)
 *      -subscibe(i ->{})
 *     	
 * Hinweis zur Erzeugung des Generators:
 *     
 *     Flowable<Integer> sourceFlowable = Flowable.generate(
 *     		() -> new AtomicInteger(0), 
 *     			(state, emitter) -> 
 *     			{
 *     					emitter.onNext(state.incrementAndGet());
 *     			}
 *     	);
 *     
 * Das AtomicInteger dient dabei als State, um �ber die Aufrufe des �bergebenen Lambdas hinweg, Daten zu speichern.    	
 *  
 * 
 * b) Zeigen Sie, dass dieser Stream Back-Pressure realisiert
 * 
 *     
 */
public class GeneratorWithBackpressure_Solution {
		
	
	public static void main(String[] args) {
		
		System.out.println("GeneratorWithBackpressure_Solution");
		
		GeneratorWithBackpressure_Solution instance = new GeneratorWithBackpressure_Solution();
		
		// a)
		//instance.a_generator();
		
		// b)
		instance.b_generatorBackPressure();
		
		
		// Wait for completion of threads
		sleep(20000);

	}

	/*
	 * a) Erzeugen Sie eine Stream Source unter Verwendung von Flowable.generate() 
	 * 
	 *  
	 * This method demonstrates usage of Flowable.generate(initialState, generator)
	 * - initialState is a Callable that provides initial State. The returnded state object is reused between calls to the generator.
	 * - generator is a BiFunction called with the state and an emitter. 
	 * - The emitter is the interface to the Flowabe-Pipeline
	 */
	void a_generator() {
		
		System.out.println("GeneratorWithBackpressure_Solution.a_generator()");
	
		// Generator
		Flowable<Integer> sourceFlowable = Flowable.generate(
				() -> new AtomicInteger(0), 
				(state, emitter) -> 
				{
					emitter.onNext(state.incrementAndGet());
				}
		);
		
		
		// Stream Pipeline
		sourceFlowable
			.map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
			.subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
	}
	
	
	/*
	 * b)
	 * 
	 */
	void b_generatorBackPressure() {
		
		System.out.println("GeneratorWithBackpressure_Solution.b_generatorBackPressure()");
		
		
		// Generator
		Flowable<Integer> sourceFlowable = Flowable.generate(
				() -> new AtomicInteger(0), 
				(state, emitter) -> 
				{
					emitter.onNext(state.incrementAndGet());
				}
		);
		
		// Stream-Pipeline
		sourceFlowable
			.map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
	
			// TODO 1: use observeOn() to decouple downstream pipeline
			.observeOn(Schedulers.computation()) 
	
			// TODO 2: slow down subscriber, to show back-pressure
			.subscribe(element ->{
				System.out.println(" Subscriber received " + element + " " + Thread.currentThread());
				sleep(100);
			});
				
	}
	
		
	
/////////////// HELPER /////////////////////
	


	static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
