package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_4_concurrency_memory_backpressure.base;

import io.reactivex.rxjava3.core.Observable;

/*
 *	Zeigen Sie, dass asynchrone Stufen in einem Stream dazu fr�hen k�nnen, dass unbegrenzt viele Elemente in einer Queue
 * 	gehalten werden. Zeigen Sie ferner, dass dies zum Absturz des Programmes fr�hen kann, wenn diese Elemente viel Memory beanspruchen. Beheben Sie dies durch Back-Pressure
 * 
 * 	L�sungsschritte:
 * 
 * 1. Erzeugen Sie eine Pipeline mit einem Observable als Source: Observable.range(1, 100_000_000_0)
 * 2. Geben Sie in einer ersten Map-Stufe f�r jedes Element des Streams ein speicherintensives Objekt zur�ck, z.B. int[10000].
 * 3. Geben Sie der Pipeline eine zweite Map-Stufe, und verlangsamen Sie diese 
 * 4. Entkoppeln Sie die beiden Stufen mit .observeOn(Schedulers.computation())
 * 5. Zeigen Sie, dass Sie ihr Programm dadurch zum Absturz bringen k�nnen (OutOfMemoryError)
 * 6. Ersetzen Sie nun das Observable durch ein Flowable und beheben damit das Problem
 * 
 * Hinweise zur Umsetzung: 
 * - Sie k�nnen die Klasse MemoryExpensiveObject benutzen um speicherintensive Objekte zu erzeugen
 * - Sie k�nnen die Methode sleep(1) benutzen, um eine Map-Stufe zu verlangsamen
 * 
 * Lernziel: Erkennen, dass Back-Pressure ein fundamentales Mittel ist, um Risiken hinsichtlich des
 * Ressourcenverbrauches entgegenzuwirken
 * 
 * 
 */
public class ConcurrencyMemoryBackpressure_Base {

	public static void main(String[] args) {
		
		ConcurrencyMemoryBackpressure_Base instance = new ConcurrencyMemoryBackpressure_Base();
		
		
		// Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
		instance.asynchronousMemeoryRisk();
	}
	
	
	/*
	 * Risiken der Asynchronit�t bei unbegrenztem Buffer: Memeory
	 * im Falle von Observable sind Producer und Consumer durch eine unbegrenzte Queue miteinander verbunden.
	 * 
	 * Dies kann im Falle eines langsamen Consumers zu unendlichem Speicherbedarf f�hren
	 * 
	 * WICHTIG: VM-Argument
	 * -Xmx32G
	 */
	private void asynchronousMemeoryRisk() {
		
		System.out.println("asynchronousMemeoryRisk() ");
		
		long start = System.currentTimeMillis();
		
		// TODO 1: an einer geeigneten Stelle, die Pipeline-Stufen mit .observeOn(Schedulers.computation()) entkoppeln
		// TODO 2: an einer geeigneten Stelle ein speicherintensives Objekt erzeugen, und in die Pipeine hineingeben
		// TODO 3: an einer geeigneten Stelle die Pipeline verlangsamen
		Observable<Integer> source = Observable.range(1, 100_000_000_0);
		
		source
		.map( i -> {
			return i;
		})
		
		// slow second map
		.map( expensiveObject -> {
			System.out.println("Slow map processed: " + expensiveObject+ " in " + Thread.currentThread());
			return expensiveObject;
		})
		
		.subscribe( expensiveObject -> {
			System.out.println("Subscriber received: " + expensiveObject + " in " + Thread.currentThread() + " time: " + (System.currentTimeMillis() - start));
		});
		
		
		sleep(10);
		
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
