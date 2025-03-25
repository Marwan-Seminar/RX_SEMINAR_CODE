package seminar_reactive_streams.exercises.rx_2_api.rx_2_7_generator_with_backpressure.base;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;

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
public class GeneratorWithBackpressure_Base {
		
	
	public static void main(String[] args) {
		
		System.out.println("GeneratorWithBackpressure_Base");
		
		GeneratorWithBackpressure_Base instance = new GeneratorWithBackpressure_Base();
		
		instance.a_generator();
		
		//instance.b_generatorBackPressure();
		
		
		// Hack to wait for completion of threads
		sleep(10000);

	}

	/*
	 * a) Erzeugen Sie eine Stream Source unter Verwendung von Flowable.generate() 
	 * 
	 *  
	 * Usage of Flowable.generate(initialState, generator):
	 * 
	 * - initialState is a Callable that provides initial State. The returned state object is reused between calls to the generator.
	 * - generator is a BiFunction called with the state and an emitter. 
	 * - The emitter is the interface to the Flowabe-Pipeline
	 * 
	 * Flowable.generate(
	 * 		() -> new AtomicInteger(0), 
	 * 		(state, emitter) -> {
	 * 			emitter.onNext(state.incrementAndGet());
	 * 		}
	 * 	);
	 */
	void a_generator() {
		
		System.out.println("GeneratorWithBackpressure_Base.a_generator()");
	
		// TODO: Generator bauen, nach obiger Anleitung
		Flowable<Integer> sourceFlowable = null; 
		
		
		// Stream Pipeline
		sourceFlowable
			.map(i -> { System.out.println("map() " + i + " " + Thread.currentThread())  ; return i;})
			.subscribe(i ->  System.out.println("subscribe() " +  i  + " " + Thread.currentThread()));
	}
	
	
	/*
	 *  b) Zeigen Sie, dass dieser Stream Back-Pressure realisiert
	 * 
	 */
	void b_generatorBackPressure() {
		
		System.out.println("GeneratorWithBackpressure_Base.b_generatorBackPressure()");
		
		
		System.out.println("a_generatorFlow");
		
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
	
			// TODO 1: use observeOn() to decouple downstream pipeline:  
	
			// TODO 2: slow down subscriber, to show back-pressure
			.subscribe(element ->{
				System.out.println(" Subscriber received " + element + " " + Thread.currentThread());
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

