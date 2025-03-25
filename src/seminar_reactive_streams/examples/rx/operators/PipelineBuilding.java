package seminar_reactive_streams.examples.rx.operators;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/*
 * How to build various Stream Pipelines using Operators
 * 
 */
public class PipelineBuilding {

	public static void main(String[] args) {
		
		PipelineBuilding instance = new PipelineBuilding();
		
		//instance.basicPipeline();
		
		instance.mergingPipelines();
	}

	void basicPipeline() {


		Observable<Integer> sourceObservable = Observable.range(1, 100);
		
		Observable<String> mappedObservable = sourceObservable.map(i ->  String.valueOf(i) );
		
		mappedObservable.subscribe(System.out::println);
		
		
	}
	
	void mapFilterSubscribePipeline() {


		Observable.range(1, 100)
		
		.map(i -> i * i)
		
		.filter(j -> j%2 == 0)
		
		.subscribe(System.out::println);
		
		
	}
	
	void mergingPipelines() {


	
		Observable<Integer> evenStream = 
		Observable.range(1, 100)
		.subscribeOn(Schedulers.computation())
		.filter(j -> j%2 == 0)
		.map(i -> {
			//System.out.println("map even:" + i  + " " + Thread.currentThread());
			cpuIntesiveCall(100);
			return i;
		});
		
		
		
		Observable<Integer> oddStream = 
		Observable.range(1, 100)
		.subscribeOn(Schedulers.computation())
		.filter(j -> j%2 == 1)
		.map(i -> {
			//System.out.println("map odd:" + i + " " + Thread.currentThread());
			cpuIntesiveCall(100);
			return i;
		});
		

		evenStream.mergeWith(oddStream)
		
		.subscribe(i -> {System.out.println("Subscribe: " + i  + " " + Thread.currentThread());});
		
		sleep(50);
	}
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
	
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
