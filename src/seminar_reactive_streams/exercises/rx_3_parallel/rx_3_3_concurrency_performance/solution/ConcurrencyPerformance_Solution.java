package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_3_concurrency_performance.solution;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 * Zeigt das Verhalten von observeOn(), mit dem Ziel die PErformance eines Streams durch Asynchronit�t zu erh�hen.
 * 
 * F�lle:
 * 
 * 1. Sequentielle Pipeleine
 * 2. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
 * 3. Performance: Langlaufende Auufgaben in den Stufen: Mit entkopplung durch observeOn() schneller als sequenitell
 * 		(hier muss die Zeile einkommentiert werden, die mit TODO markiert ist, um die Nebenl�ufigkeit zu aktivieren.)
 */
public class ConcurrencyPerformance_Solution {

	public static void main(String[] args) {
		
		System.out.println("ConcurrencyPerformance_Solution");
		
		ConcurrencyPerformance_Solution instance = new ConcurrencyPerformance_Solution();
		
		// 1. Sequentielle Pipeleine
		//instance.sequentialPipeline();
		
		// 2. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
		//instance.asynchronousPipeline();
		
		// 3. Performance durch observeOn(): Langlaufende Aufgaben in den Stufen: Mit Entkopplung durch observeOn() schneller als sequenitell
		instance.asynchronousPipelinePerformance();
		
	}


	void sequentialPipeline() {
		
		System.out.println("ConcurrencyPerformance_Solution.sequentialPipeline()");
		
		Observable<Integer> source = Observable.range(1, 100);
		
		source
		.map( i -> {
			System.out.println("map 1: " + i + " in " + Thread.currentThread());
			return i;
		})
		.map( i -> {
			System.out.println("map 2: " + i + " in " + Thread.currentThread());
			return i;
		})
		.subscribe( i -> {
			System.out.println("subscribe: " + i + " in " + Thread.currentThread());
		});
		
		
	}
	
	/*
	 *  2. Asynchrone Pipeline mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
	 */
	void asynchronousPipeline() {
		
		System.out.println("ConcurrencyPerformance_Solution.asynchronousPipeline()");
		
		Observable<Integer> source = Observable.range(1, 100);
		
		source
		.map( i -> {
			System.out.println("map 1: " + i + " in " + Thread.currentThread());
			return i;
		})
		
		// Call to observeOn() decouples pipeline stages
		.observeOn(Schedulers.computation())
		
		.map( i -> {
			System.out.println("map 2: " + i + " in " + Thread.currentThread());
			return i;
		})
		.subscribe( i -> {
			System.out.println("subscribe: " + i + " in " + Thread.currentThread());
		});
		
		sleep(10);
	}
	
	
	
	/*
	 * 3. Performance durch observeOn(): 
	 * Langlaufende Aufgaben in den Stufen: Mit Entkopplung durch observeOn() schneller als sequenitell
	 * 
	 * Den Unterschied in der ZEitmessung kann man sehen, indem man observeOn() auskommentiert
	 */
	void asynchronousPipelinePerformance() {
		
		System.out.println("ConcurrencyPerformance_Solution.asynchronousPipelinePerformance()");
		
		long start = System.currentTimeMillis();
		
		Observable<Integer> source = Observable.range(1, 100);
		
		source
		.map( i -> {
			// cpu intensive call: 100 Milliseconds
			cpuIntesiveCall(100);
			
			System.out.println("map 1: " + i + " in " + Thread.currentThread());
			return i;
		})
		
		// Call to observeOn() decouples pipeline stages: Increases performance, if stages are long-running
		// TODO: comment this call to see the different behavior
		.observeOn(Schedulers.computation())
		
		.map( i -> {
			// cpu intensive call: 100 Milliseconds
			cpuIntesiveCall(100);
			System.out.println("map 2: " + i + " in " + Thread.currentThread());
			return i;
		})
		.subscribe( i -> {
			System.out.println("subscribe: " + i + " in " + Thread.currentThread());
			if(i == 100) {
				// Hack: This time measurement is not correct, as elements arrive out of order!
				System.out.println("Pipeline finisched after : " + (System.currentTimeMillis() - start));
			}
		});
		
		
		sleep(25);
	}
	

	
	/////////////////// HELPER ////////////////////////////
	
	/*
	 * This method runs  
	 * runtimeInMillis
	 * Milliseconds on a CPU without blocking, then it returns.
	 */
	 static void cpuIntesiveCall(long runtimeInMillis) {
		 long start = System.currentTimeMillis();
		 long dummy = 3;
		 while(true) {
			 dummy = (dummy + dummy);
			 if (dummy > Integer.MAX_VALUE) {
				 dummy = dummy %  Integer.MAX_VALUE;
				 if (System.currentTimeMillis() - start > runtimeInMillis){
					 return;
				 }
			 }
			 

		 }
	 }
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
}
