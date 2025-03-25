package seminar_reactive_streams.examples.rx.parallel.para_seq;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 * Example for parallel() / sequential() interface of Flwoable
 */
public class ParallelSequential {

	
	public static void main(String[] args) {
		
		ParallelSequential instance = new ParallelSequential();
		
		instance.para_seq();
	}

	void para_seq() {
	
		
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
			System.out.println("Subscribe " + i + " " + Thread.currentThread());
		
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
