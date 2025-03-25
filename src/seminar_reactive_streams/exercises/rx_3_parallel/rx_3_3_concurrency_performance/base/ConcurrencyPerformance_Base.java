package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_3_concurrency_performance.base;

import io.reactivex.rxjava3.core.Observable;

/*
 * RX 3.3 Performance durch Nebenl�ufigkeit
 * Stream durch observeOn() beschleunigen
 * 
 * Erzeuge Sie einen Stream, mit lang laufenden Stream-Stufen.
 * Entkoppeln Sie diese Stufen durch observeOn().
 * Zeigen Sie, dass ihr Stream sich dadurch beschleunigt.
 * 
 * Benutzen Sie als Ausgangsstream z.B:
 * Observable.range(0,100).map(i->i).map(i->i)
 * 
 * Gehen Sie dann in folgenden zwei Schritten vor:

 * 
 * a) 	Sorgen Sie daf�r, dass die beiden Map-Stufen asynchron zueinander ausgef�hrt werden.
 *  	Zeigen Sie das durch Shell-Ausgaben, die die jeweiligen Threads ausgeben.
 *  	�sungshinweis: observeOn(Scheduler.computing()) 
 * 
 * b)	F�gen Sie in beide Map-Stufen eine langlaufende Funktion ein, und zeigen Sie,
 * 		dass die asynchrone Version schneller ist, als die sequentielle.
 *  	Wie viel schneller kann sie werden?
 *  	Hinweis: Sie k�nnen die langlaufende Funktion cpuIntesiveCall() aus der Musterl�sung verwenden.
 * 
 * Lernziel: Erkennen, dass eine asynchroner Stream einen Performance-Vorteil bieten kann  
 */
public class ConcurrencyPerformance_Base {

	public static void main(String[] args) {
		
		System.out.println("ConcurrencyPerformance_Base");
		
		ConcurrencyPerformance_Base instance = new ConcurrencyPerformance_Base();
		
		// Sequentielle Pipeleine
		instance.sequentialPipeline();
		
		
		
	}


	/*
	 * Diese Methode enth�lt eine sequentiellen Stream mit zwei Map-Stufen und eine Zeitmessung
	 * 
	 * TODO 1: An der richtigen Stelle .observeOn(Schedulers.computation()) einf�gen
	 * TODO 2: In den Map-Calls eine langlaufende Funtkion einf�gen, z.b. cpuIntesiveCall(100)
	 * TODO 3: Zeigen, dass der Stream mit observeOn() schneller ist als ohne.
	 */
	void sequentialPipeline() {
		
		System.out.println("ConcurrencyPerformanceBase.sequentialPipeline()");
		
		Observable<Integer> source = Observable.range(1, 100);
		
		long start = System.currentTimeMillis();
		
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
			if(i == 100) {
				// Hack: This time measurement is not correct, as elements arrive out of order!
				System.out.println("Pipeline finisched after : " + (System.currentTimeMillis() - start));
			}
		});
		
		// Sleep call is helpful in asynchronous case
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
