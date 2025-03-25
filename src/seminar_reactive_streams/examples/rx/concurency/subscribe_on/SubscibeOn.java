package seminar_reactive_streams.examples.rx.concurency.subscribe_on;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
*/

/*
 * Zeigt das Verhalten von subscirbeOn()
 * 
 * F�lle:
 * 1. Observable mit einem Subscriber ohne subscribeOn()
 * 2. Observable mit einem Subscriber und subscribeOn()
 * 3. Observable mit mehreren Subscribern und subscribeOn()
 * 4. Performance durch subscribeOn(): TODO
 */
public class SubscibeOn {
	
	public static void main(String[] args) {
		
		SubscibeOn instance = new SubscibeOn();
		
		//  1. Observable mit einem Subscriber ohne subscribeOn()
		//instance.observableWithSingleObserver();
		
		// 2. Observable mit einem Subscriber und subscribeOn()
		//instance.subscribeOnObservableWithSingleObserver();
		
		//  3. Observable mit mehreren Subscribern und subscribeOn()
		instance.subscribeOnObservableWithSeveralObservers();
	}
	
	/*
	 * Fall 1: 
	 * Observable mit einem einzige Subscriber und  OHNE subscribeOn()
	 * L�uft sequentiell im Main Threaad
	 */
	void observableWithSingleObserver() {
		
		Observable<Integer> source = Observable.range(0, 100);
		
		source
		
		.map(i -> {
			System.out.println("map " + i + " called in " + Thread.currentThread());
			return i;
		})
		.subscribe(i ->{
			System.out.println("subscribe " + i + " called in " + Thread.currentThread());
		
		});
		
	}

	/*
	 * Fall 2: 
	 * Observable mit einem einzige Subscriber und subscribeOn()
	 * L�uft sequentiell in einem Threaad aber unab�ngig vom Main Thread
	 */
	void subscribeOnObservableWithSingleObserver() {
		
		Observable<Integer> source = Observable.range(0, 100);
		
		source.
		subscribeOn(Schedulers.computation())
		.map(i -> {
			System.out.println("map " + i + " called in " + Thread.currentThread());
			return i;
		})
		.subscribe(i ->{
			System.out.println("subscribe " + i + " called in " + Thread.currentThread());
		
		});
		
		sleep(2000);
	}
	

	/*
	 * Fall 3 
	 * Observable mit mehreren Subscribern und subscribeOn()
	 * 
	 * Hier h�ngt das Verhalten ganz wesentlich davon ab, ob subscribeOn() benutzt wird.
	 * Mit subsribeOn() laufen die beiden Pipelines unabh�ngig, also asyhncron zueinander.
	 * Ohne subscribeOn() laufen sie nacheinenader!
	 */
	void subscribeOnObservableWithSeveralObservers() {
		
		Observable<Integer> source = Observable.range(0, 100);
		
		// mit subscribeOn()
		Observable<Integer> scheduledObservable = source.subscribeOn(Schedulers.computation());
		
		// ohne subscribeOn()
		//Observable<Integer> scheduledObservable = source;
		
		// 1. Pipeline
		scheduledObservable
		.map(i -> {
			System.out.println("map pipeline 1: " + i + " called in " + Thread.currentThread());
			return i;
		})
		.subscribe(i ->{
			System.out.println("subscribe pipeline 1: " + i + " called in " + Thread.currentThread());
		
		});
		
		// 2. Pipeline
		scheduledObservable
		.map(i -> {
			System.out.println("map pipeline 2: " + i + " called in " + Thread.currentThread());
			return i;
		})
		.subscribe(i ->{
			System.out.println("subscribe pipeline 2: " + i + " called in " + Thread.currentThread());
		
		});
		
		/*
		// Dasselbe in kurz
		// 1. Pipeline
		scheduledObservable
		.map(i -> i)
		.subscribe(i ->	System.out.println( "subscribe pipeline 1" + i ));
		
		
		// 2. Pipeline
		scheduledObservable
		.map(i -> i)
		.subscribe(i ->	System.out.println( "subscribe pipeline 2" + i ));
		 */	
		
		sleep(10);
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
