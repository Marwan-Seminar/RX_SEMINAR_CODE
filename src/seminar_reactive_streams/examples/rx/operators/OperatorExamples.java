package seminar_reactive_streams.examples.rx.operators;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

/*
import io.reactivex.Flowable;
import io.reactivex.Observable;

*/

public class OperatorExamples {

	public static void main(String[] args) {
		
		OperatorExamples instance = new OperatorExamples();
		instance.creationOperators();
	}

	 void creationOperators() {
	
		 Observable.create(null);
		 
		 //Flowable.create(source, mode)
	}
	 
	 void filteringOperators(){
		 Observable o = null;
		 o.filter(null);
		 o.distinct();
	 }
	 
	 void mapOperator(){
		 
		Observable<Integer> source = Observable.range(0, 10);
		 
		Observable<String> mappedObservable = source.map((Integer i) -> String.valueOf(i));
		 
		mappedObservable.subscribe(System.out::println);
	 }
	 
	 SomeObject f() {
		 return null;
	 }
	 void backpressureOperators() {
		 
		 Flowable f = null;
		 f.onBackpressureBuffer();
		 f.onBackpressureDrop();
		 f.onBackpressureLatest();
		 
		
		 
	 }
	 
	 void aggregationOperators() {
		 Flowable flow = null;
		 Observable obs = null;
		 
		 Observable.concat((Observable<Integer>)null, (Observable<Integer>) null);
		 
	 }
}

class SomeObject{
	
}