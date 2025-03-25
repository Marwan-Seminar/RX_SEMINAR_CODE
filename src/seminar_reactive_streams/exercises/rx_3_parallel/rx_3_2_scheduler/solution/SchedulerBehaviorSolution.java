package seminar_reactive_streams.exercises.rx_3_parallel.rx_3_2_scheduler.solution;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
 *  RX 3.2: Scheduler f�r unterschiedliche AnforderungenSchedulers.computing und Schdulers.io()
 *  
 *  Zeigen Sie, dass der Scheduler Schedulers.computation() nur eine feste Anzahl von Threads verwendet, auch wenn mehr Aufgaben als Threads vorhanden sind. 
 *  Zeigen Sie, dass Schedulers.io() sich anders verh�lt, indem er weitere Threads startet
 *  
 *  Verwenden Sie daf�r folgendes Vorgehen
 *  
 *  - Bauen Sie einen Stream, der Schedulers.computaion() verwendet (subscribeOn(Schedulers.computation()))
 *  - Melden Sie an diesem Stream viele Subscriber an, mehr als Ihr Rechner CPU-Kerne hat, z.B. 10 Subscriber bei 8 Kernen
 *  - Sorgen Sie daf�r, dass jeder Subscriber sehr lange l�uft, eventuell unendlich lange. 
 *  - Zeigen Sie, dass nicht alle Subscriber zum Zuge kommen.
 *  - Ver�ndern Sie dieses Verhalten, indem Sie Schedulers.io() anstelle von Schedulers.computation() verwenden.
 *  - Welches Risiko besteht aber nun?
 *  
 *  Lernziel: Das unterschiedliche Verhalten der beiden Scheduler in Grenzsituationen verstehen
 */
public class SchedulerBehaviorSolution {
	
	public static void main(String[] args) {
		
		SchedulerBehaviorSolution instance = new SchedulerBehaviorSolution();
		
		instance.computationScheduler();
				
		sleep(1000000);
	}

	/*
	 * Zeigt, dass der Computational Scheduler fixed size ist, der io Scheduler variabel.
	 * 
	 * Fall 1 Computational Scheduler
	 * 		Es werden mehr Subscriber gestartet, als Threads im Computational-Scheduler.
	 * 		Der Computational Scheduler	enth�lt typischerweise NR_OF_CORES viele Threads.
	 * 	 	Falls manche Subscriber alle Worker mit CPU-intensiven Aufgaben auslasten,
	 * 		verhungern somit weitere Subscriber.
	 * 		(Ein solches Verhalten der Subscriber widerspricht jedoch der Regel X? der R-S Spezifikation)
	 * 
	 * Fall 2 IO-Scheduler
	 * 		Der IO Scheuler verh�lt sich anders, er startet solange weitere Threads, 
	 * 		bis alle ARbeitsauftr�ge bedient sind. Das ist jedoch nur vordergr�ndig besser, 
	 * 		da das Risiko besteht, dass durch die Instanziierung zu vieler Threads zu viele Ressourcen
	 * 		verbraucht werden (Memory etc.).
	 */
	void computationScheduler() {
	
		int NR_OF_CORES = 8;
		
		Observable<Integer> source = Observable.range(1, 5);
		
		source = source.
				// Fall 1: computation()
				subscribeOn(Schedulers.computation());
				// Fall 2: io()
				//subscribeOn(Schedulers.io());
						
		for(int coreIdx = 0; coreIdx <  2 * NR_OF_CORES ; coreIdx ++) {
			
			final int coreIdxFinal = coreIdx;
			
			source.subscribe( i -> {
				System.out.println("Subscriber " + coreIdxFinal + " running on " + Thread.currentThread() );
				cpuIntesiveCall(100000);
			});
		}
			
	}
	
/////////////// HELPER /////////////////////
	

	
	static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
