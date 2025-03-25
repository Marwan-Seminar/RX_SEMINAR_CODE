package seminar_reactive_streams.exercises.rx_1_standard.rx_1_1_minimal_stream_rx.base;

/*
 * RX 1.1: Minimal Rx-Stream
 * Einen ganz einfachen Stream bauen
 * 
 * 
 * Bauen Sie einen einfachen Stream mit RxJava.
 * 
 * Ein Observable versendet die Zahlen 1-100, ein Subscriber empf�ngt sie, und schreibt sie auf die Shell.
 * 
 * Hinweise: 
 * 	- Observable.range(1, 100) gibt ein Observable zur�ck, das das die Daten emittiert
 * 	- Observable.subscribe(System.out::printl) realieisert einen Subscriber, der die Daten auf die Shell schreibt.
 * 
 * Lernziel: Stream Programmierung kennenlernen
 */
public class MinimalStreamRx_Base {

	public static void main(String[] args) {
		
		System.out.println("MinimalStreamRx_Solution");
		
		MinimalStreamRx_Base instance = new MinimalStreamRx_Base();
		
		instance.simpleStreamRx();
	}

	private void simpleStreamRx() {
	
		System.out.println("MinimalStreamRx_Solution.simpleStreamRx()");
		
		// TODO: Benutzen Sie Observable.range(1, 100); um eine Stream-Source zu erzeugen, die die Zahlen 1-100 emittiert
		
		// TODO melden Sie �ber den Methodenaufruf .subscribe(System.out::println); einen Subscriber an diese Source an.
	}
}
