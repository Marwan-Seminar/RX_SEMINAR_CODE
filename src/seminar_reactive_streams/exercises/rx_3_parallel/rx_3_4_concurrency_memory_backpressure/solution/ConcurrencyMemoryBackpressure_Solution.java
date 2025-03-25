package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_4_concurrency_memory_backpressure.solution;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 * Zeigt die Relevanz von Back-Pressure in einer asynchronen Pipeline.
 * 
 * 1. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
 * 2. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memory-Verbrauch
 * 3. L�sung des Problems durch Flowable und Back-Pressure
 * 
 */
public class ConcurrencyMemoryBackpressure_Solution {

	public static void main(String[] args) {
		
		ConcurrencyMemoryBackpressure_Solution instance = new ConcurrencyMemoryBackpressure_Solution();
		
		
		// 1. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
		//instance.asynchronousPipeline();
		
		
		// 2. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memory Overflow
		//instance.asynchronousMemoryRisk();
		
		// 3. Flowable behebt die Riskiken des unbounded Buffers durch Back-Pressure
		instance.asynchronousMemorySolution();
	}



	
	/*
	 *  1. Asynchrone Pipeline mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
	 */
	void asynchronousPipeline() {
		
		System.out.println("asynchronousPipeline() ");
		
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
		
		
		sleep(15);
	}
	
	
	
	
	
	/*
	 * 4. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
	 * im Falle von Observable sind Producer und Consumer durch eine unbegrenzte Queue miteinander verbunden.
	 * 
	 * Dies kann im Falle eines langsamen Consumers zu unendlichem Speicherbedarf f�hren
	 * 
	 * WICHTIG: VM-Argument
	 * -Xmx32G
	 */
	private void asynchronousMemoryRisk() {
		
		System.out.println("asynchronousMemeoryRisk() ");
		
		long start = System.currentTimeMillis();
		
		
		Observable<Integer> source = Observable.range(1, 100_000_000_0);
		
		source
		.map( i -> {
			MemoryExpensiveObject newExpensiveObject = new MemoryExpensiveObject(i);
			//if(i % 1000 == 0) {
				System.out.println("Created : " + newExpensiveObject + " in " + Thread.currentThread() + " time: " + (System.currentTimeMillis() - start) );
			//}
			return newExpensiveObject;
		})
		
		// Call to observeOn() decouples pipeline stages: Increases performance, if stages are long-running
		.observeOn(Schedulers.computation())
		
		// slow second map
		.map( expensiveObject -> {
			// sleep 1 second
			sleep(1);
			System.out.println("Slow map processed: " + expensiveObject+ " in " + Thread.currentThread());
			return expensiveObject;
		})
		.subscribe( expensiveObject -> {
			System.out.println("Slow Subscriber received: " + expensiveObject + " in " + Thread.currentThread() + " time: " + (System.currentTimeMillis() - start));
		});
		
		
		sleep(10);
		
	}
	
	/*
	 * // 3. Flowable behebt die Risiken des unbounded Buffers durch Back-Prassure
	 */
	private void asynchronousMemorySolution() {
		
		System.out.println("asynchronousMemeorySolution() ");
		
		long start = System.currentTimeMillis();
		
		//AtomicInteger counter = new AtomicInteger();
		
		Flowable<Integer> source = Flowable.range(1, 1000000000);
		
		source
		.map( i -> {
			MemoryExpensiveObject newExpensiveObject = new MemoryExpensiveObject(i);
			//if(i % 1000 == 0) {
				System.out.println("Created : " + newExpensiveObject );
			//}
				
			return newExpensiveObject;
		})
		
		// Call to observeOn() decouples pipeline stages: Increases performance, if stages are long-running
		.observeOn(Schedulers.computation())
		
		// slow second map
		.map( expensiveObject -> {
			// CPU Time is used  (10 MS) (upper examples use sleeping instead)
			cpuIntesiveCall(10);
			//System.out.println("Slow map processed: " + expensiveObject);
			return expensiveObject;
		})
		.subscribe( expensiveObject -> {
			System.out.println("Slow Subscriber received: " + expensiveObject );
		});
		
		sleep(100);
		
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

class MemoryExpensiveObject{
	
	int[] data = new int[1000_000];
	
	int id;
	
	MemoryExpensiveObject(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MemoryExpensiveObject: " + id;
	}
}
