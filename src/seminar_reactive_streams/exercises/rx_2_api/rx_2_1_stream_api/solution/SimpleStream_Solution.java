package seminar_reactive_streams.exercises.rx_2_api.rx_2_1_stream_api.solution;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

/*
 * Bauen Sie einen einfachen Stream mit Observable oder Flowable als Source. 
 * 
 * a) Diese Source versendet die Zahlen 1-100, ein Subscriber empf�ngt sie, und schreibt sie auf die Konsole
 * 
 * b) Bauen Sie mehrere Stufen in den Stream ein, nutzen Sie map() um die Quadratzahlen zu bilden filter() um die geraden Quadratzahlen zu finden
 * 
 * c) Addieren Sie die Zahlen von 1-100, nutzen Sie daf�r reduce()
 * 
 * 
 * L�sungshinweis
 *  	Flowable.range()
 *  	  map(i -> i / 2)
 *  	  filter (i -> (i%2) == 0)
 *  	  subsribe(println)
 */

public class SimpleStream_Solution {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("SimpleStream_Solution()");
		
		SimpleStream_Solution instance = new SimpleStream_Solution();
		
		// a)
		instance.a_simpleStreamPrint();
		
		// b)
		instance.b_simpleStreamSquares();
		
		// c
		instance.c_simpleStreamSumOneToHundred();
		
	}

	/*
	 * a) Diese Source versendet die Zahlen 1-100, ein Subscriber empf�ngt sie, und schreibt sie auf die Konsole
	 */
	void a_simpleStreamPrint() {
		
		System.out.println("SimpleStream_Solution.a_simpleStreamPrint()");
		
		// Datenquelle erzeugen
		Observable<Integer> source = Observable.range(1, 100);

		// Einen Subscriber an dieser Datenquelle anmelden
		source
			.subscribe(i -> System.out.println(i));
		
		
	}

	
	/*
	 * b) 	Bauen Sie mehreren Stufen in den Stream ein,
	 *  	nutzen Sie map() um die Quadratzahlen zu bilden
	 *  	filter() um die geraden Quadratzahlen zu finden.
	 */
	void b_simpleStreamSquares() {
		
		System.out.println("SimpleStream_Solution.b_simpleStreamSquares()");
		
		// Datenquellen k�nnen Flowable oder Observable sein
		//Flowable.range(0, 100)
		Observable.range(1, 100)
		
		// map() bildet Elemente des Streams ab
		.map(i -> i * i)
	
		// filter sendet nur die Elemente weiter, die dem �bergebenen Kriterium entsprechen
		.filter (i -> (i%2) == 0)
		
		.subscribe(System.out::println);
		
	}
	
	/*
	 *  c) Addieren Sie die Zahlen von 1-100, nutzen Sie daf�r reduce()
	 */
	void c_simpleStreamSumOneToHundred() {
		
		System.out.println("SimpleStream_Solution.c_simpleStreamSumZeroToHundred()");
		
		// Ein Maybe ist eine Stream-Stufe, die ein einzenes Element liefert, oder keines.
		Maybe<Integer> maybeResult =
				Observable.range(1, 100)
					.reduce((a,b) -> a+b);
		
		maybeResult.subscribe(i -> { System.out.println(" Sum of 0 to  100: " + i );});
		
	}
	
	
		
}
