package seminar_reactive_streams.examples.rx.schedulers;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 * Demostrates useage of Schedulers in RxJava
 * 
 * Es ist gar nicht so leicht, den Scheudler aus der Ruhe zu bringen:
 * 
 * Selbsrt wenn ich viele Tausend Aufrufe in den IO Scheduler stecke, erzeugt er maximal ca. 128 Threads
 */
public class SchedulersRx {

	public static void main(String[] args) {
		
		SchedulersRx instance = new SchedulersRx();
		
		//instance.computingSchedulerBehavior();
		instance.computingSchedulerBehaviorParallel();
		
	}
	
	void computingSchedulerBehavior() {
	
		Flowable<Integer> flowable = Flowable.range(0,100_000_000);
		
		flowable.subscribeOn(Schedulers.computation())
		.map(i -> {
			cpuIntesiveCall(1000);
			return i;
		})
		.subscribe(i ->{
			System.out.println(i + " "  + Thread.currentThread());
		});
		
		sleep(30);
	}
	
	void computingSchedulerBehaviorParallel() {
		
		Flowable<Integer> flowable = Flowable.range(0,100_000_000);
		
		//flowable.subscribeOn(Schedulers.computation())
		flowable
		.flatMap(i -> 
				 Flowable.just(i)
				.subscribeOn(Schedulers.io())
				.map(j -> {
								cpuIntesiveCall(1000);
								System.out.println(" map " + i + " "  + Thread.currentThread());
								return j;
							})
			
		)		
		.subscribe(i ->{
			System.out.println("subscribe " + i + " "  + Thread.currentThread());
		});
		
		sleep(30);
	}
	
	
	void schedulerTypes() {
		
		Schedulers.io();
		
		Schedulers.computation();
		
		Schedulers.single();
	}
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
	
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
	
	
}
