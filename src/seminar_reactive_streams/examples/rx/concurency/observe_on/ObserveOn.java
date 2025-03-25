package seminar_reactive_streams.examples.rx.concurency.observe_on;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

//import io.reactivex.schedulers.Schedulers;

/*
 * Zeigt das Verhalten von observeOn()
 * 
 * F�lle:
 * 
 * 1. Sequentielle Pipeleine
 * 2. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
 * 3. Performance: Langlaufende Auufgaben in den Stufen: Mit entkopplung durch observeOn() schneller als sequenitell
 * 4. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
 */
public class ObserveOn {

	public static void main(String[] args) {
		
		ObserveOn instance = new ObserveOn();
		
		// 1. Sequentielle Pipeleine
		instance.sequentialPipeline();
		
		// 2. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
		//instance.asynchronousPipeline();
		
		// 3. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
		//instance.asynchronousMemeoryRisk();
		
		// 3. Flowable behebt die Riskiken des unbounded Buffers durch Back-Prassure
		//instance.flowableAsynchPipeline();
	}


	void sequentialPipeline() {
		
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
	 *  2. Asynchrone Pipeleine mit observeOn(): Die Stufen der Pipeline werden asynchron zueinander ausgef�hrt
	 */
	void asynchronousPipeline() {
		
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
	 * 3. Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
	 * im Falle von Observable sind Producer und Consumer durch eine unbegrenzte Queue miteinander verbunden.
	 * 
	 * Dies kann im Falle eines langsamen Consumers zu unendlichem Speicherbedarf f�hren
	 * 
	 * WICHTIG: VM-Argument
	 * -Xmx32G
	 */
	private void asynchronousMemeoryRisk() {
		
		System.out.println("asynchronousPerformancePipeline() ");
		
		long start = System.currentTimeMillis();
		
		//AtomicInteger counter = new AtomicInteger();
		
		Observable<Integer> source = Observable.range(1, 1000000000);
		
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
	 * 4 Flowable l�st das Problem durch Back-Pressure
	 */
	private void flowableAsynchPipeline() {
		
		System.out.println("asynchronousPerformancePipeline() ");
		
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
			// HERE CPU Time is used instead of sleeping (100 MS)
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
