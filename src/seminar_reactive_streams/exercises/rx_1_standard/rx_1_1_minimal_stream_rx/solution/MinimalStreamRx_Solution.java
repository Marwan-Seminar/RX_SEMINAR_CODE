package seminar_reactive_streams.exercises.rx_1_standard.rx_1_1_minimal_stream_rx.solution;

import io.reactivex.rxjava3.core.Observable;

/*
 * RX 1.1: Minimal Rx-Stream
 * Einen ganz einfachen RxJava Stream bauen
 * 
 * 
 * Ein Observable versendet die Zahlen 1-100, ein Subscriber empfaengt sie, und schreibt sie auf die Shell.
 * 
 * Hinweise: 
 * 	- Observable.range(1, 100) gibt ein Observable zurueck, das das die Daten emittiert
 * 	- Observable.subscribe(System.out::printl) realisiert einen Subscriber, der die Daten auf die Shell schreibt.
 * 
 * Lernziel: Stream Programmierung kennenlernen
 */
public class MinimalStreamRx_Solution {

	public static void main(String[] args) {
		
		MinimalStreamRx_Solution instance = new MinimalStreamRx_Solution();
		
		instance.simpleStreamRx();
	}

	private void simpleStreamRx() {
		
		Observable<Integer> source = Observable.range(1,  100);
		
		source.subscribe(System.out::println);
	}
}
