package seminar_reactive_streams.examples.rx.parallel.flatmap_grpby_obson;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ParallelFlatMapGroupBy {

	 void flapMapGroupByParallelobserveOnMINIMAL() {
			System.out.println("flapMapGroupByParallelobserveOnMINIMAL()");
		
			Observable.range(0, 100)
			// Split into  Groups
			.groupBy(i -> i % 4)
			// flatMap
			.flatMap(group -> 
				group.observeOn(Schedulers.computation())
				.map(j -> j)
			)
			.subscribe(i -> {});	
			

		}

}
