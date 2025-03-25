package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_7_automatic_para.solution;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * RX 3.7 Solution
 * 
 * Parallelisieren Sie den unten stehenden Stream unter Einsatz der API f�r die automatische Parallelisierung
 * 
 * Hinweis: Verwenden Sie folgende API calls
 * - Flowable.parallel();
 * - ParallelFlowable.runOn(Schedulers.computation())
 * -  ParallelFlowable.sequential()
 * 
 */
public class AutomaticParallelization_SOLUTION {


	public static void main(String[] args) {
		
		AutomaticParallelization_SOLUTION instance = new AutomaticParallelization_SOLUTION();
		
		instance.parallelSequantialStream();
	}


	void sequantialStream() {
	
		
		Flowable<Integer> sourceFlowable = Flowable.range(0, 100);
		
		sourceFlowable		
			.map(i ->{
				System.out.println("Sequantial-Map: " + i + " " + Thread.currentThread());
				cpuIntesiveCall(100);
				return i;
			})
		
		
		.subscribe(i -> {
			System.out.println("Sequential-Subscribe " + i + " " + Thread.currentThread());
		
		});
		
		sleep(10);
	}

	

	void parallelSequantialStream() {
	
		
		Flowable<Integer> sourceFlowable = Flowable.range(0, 100);
		
		ParallelFlowable<Integer> parallelFlowable = sourceFlowable.parallel();
		parallelFlowable.runOn(Schedulers.computation())
		
		.map(i ->{
			System.out.println("Parallel-Map: " + i + " " + Thread.currentThread());
			cpuIntesiveCall(100);
			return i;
		})
		
		.sequential()
		
		.subscribe(i -> {
			System.out.println("Parallel-Subscribe " + i + " " + Thread.currentThread());
		
		});
		
		sleep(10);
	}
	

/////////////////// HELPER ////////////////////////////

	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
	
	
	
	/*
	 * This method runs runtimeInMillis Milliseconds on a CPU without blocking, then
	 * it returns.
	 */
	static void cpuIntesiveCall(long runtimeInMillis) {
		long start = System.currentTimeMillis();
		long dummy = 3;
		while (true) {
			dummy = (dummy + dummy);
			if (dummy > Integer.MAX_VALUE) {
				dummy = dummy % Integer.MAX_VALUE;
				if (System.currentTimeMillis() - start > runtimeInMillis) {
					return;
				}
			}
		}
	}
}
