package seminar_reactive_streams.exercises.rx_2_api.rx_2_1_stream_api.base;

import io.reactivex.rxjava3.core.Observable;

/*
 * Bauen Sie einen einfachen Stream mit Observable oder Flowable als Source. 
 * 
 * a) Diese Source versendet die Zahlen 1-100, ein Subscriber empfaengt sie, und schreibt sie auf die Konsole
 * 
 * b) Bauen Sie mehrere Stufen in den Stream ein, nutzen Sie map() um die Quadratzahlen zu bilden filter() um die geraden Quadratzahlen zu finden
 * 
 * c) Addieren Sie die Zahlen von 1-100, nutzen Sie dafuer reduce()
 * 
 * 
 * Loesungshinweis
 *  	Flowable.range()
 *  	  map(i -> i / 2)
 *  	  filter (i -> (i%2) == 0)
 *  	  subsribe(println)
 */

public class SimpleStream_Base {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("SimpleStream_Base()");
		
		SimpleStream_Base instance = new SimpleStream_Base();
		
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
		
		System.out.println("SimpleStream_Base.a_simpleStreamPrint()");
		
		// TODO 1. Datenquelle erzeugen, z.B. Observable.range(1, 100); 
		
		// TODO 2. Einen Subscriber an dieser Datenquelle anmelden: .subscribe(i -> { das Element i verarbeiten});
		
		
	}

	
	/*
	 * b) 	Bauen Sie mehrere Stufen in den Stream ein,
	 *  	nutzen Sie map() um die Quadratzahlen zu bilden,
	 *  	filter() um die geraden Quadratzahlen zu finden.
	 */
	void b_simpleStreamSquares() {
		
		System.out.println("SimpleStream_Base.b_simpleStreamSquares()");
		
		// TODO 1. Datenquellen koennen Flowable oder Observable sein, z.B.: Flowable.range(0, 100)
				
		// TODO 2. map() bildet Elemente des Streams ab
		
		// TODO 3. filter() sendet nur die Elemente weiter, die einem �bergebenen Kriterium entsprechen, z.B. (i -> i%2 == 0) findet gerade Zahlen
		
		// TODO 4. subscribe(Consumer<? super T> onNext) meldet einen Subscriber an 

		
	}
	
	/*
	 *  c) Addieren Sie die Zahlen von 1-100, nutzen Sie dafuer reduce()
	 */
	void c_simpleStreamSumOneToHundred() {
		
		System.out.println("SimpleStream_Base.c_simpleStreamSumZeroToHundred()");
		
		// Ein Maybe ist der R�ckgabetyp von reduce(),  eine Stream-Stufe, die ein einzenes Element liefert, oder keines. 
		//Maybe<Integer> maybeResult = ...
		
		// TODO 1. Stream Source:
		Observable.range(1, 100);
			
		// TODO 2. reduce() benutzen, um die Elemente zu addieren.
		// reduce(BiFunction<T, T, T> reducer): Fasst die Elemente des Streams zu einem einzigen Element zusammen, 
		// indem die Funktion reducer nacheinander auf alle Elemente des Streams angewndet wird.
		// (a,b) -> a+b) ist z.B. eine solche BiFunction
		
		// TODO 3. Subscriber anmelden:
		// maybeResult.subscribe(i -> { System.out.println(" Sum of 0 to  100: " + i );});
		
	}
	
	
		
}
