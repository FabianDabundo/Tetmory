package tetmory;

public class GameLogik {
	private EffekteController effekte; // <--In Ruhe lassen!
	private KartenController kartenDeck; // <--In Ruhe lassen!
	private byte freigeschalteteMotive;
	private double generatorZehntelFrequenz;
	private int generatorFuellstand; // <--In Ruhe lassen!
	//Es d�rfen und sollen gerne weitere Variablen & Methoden erstellt werden


	/* Wird ganz am Anfang des Spiels aufgerufen, quasi wenn der
	 * Benutzer zum ersten Mal auf Start dr�ckt.  */
	public void start() {
		System.out.println("Bereite Level vor...");
		freigeschalteteMotive = 3;
		generatorZehntelFrequenz = 5; //1 = 1 Mal in 10 Sekunden, 5 = 5 Mal in 10 Sekunden, 10 = jede Sekunde, 0 = aus.
		
	}
	
	/* Wird im Sekundentakt aufgerufen, d.h. einmal in der Sekunde
	 * hat die GameLogik hier die M�glichkeit, das Spielgeschehen zu
	 * �berblicken und ggf. einzugreifen.  
	 * Kann als Zeitmesser genutzt werden (da pro Aufruf eine Sekunde vergangen)! */
	public void denke() {
	
		
	}
	
	/* Wird aufgerufen sobald der Benutzer auf eine Karte klickt,
	   Die entsprechende KartenID wird als Paramter bereitgestellt */
	public void verarbeiteEingaben(short kartenId) {
		//effekte.spieleSound((byte)1);
		kartenDeck.flippeKarte(kartenId);
		
		
	}

/* ANLEITUNG & HILFE F�R PROGRAMMIERER DER GAMELOGIK (z.B. Fahrbahn...) :

Hier eine �bersicht der Einflussm�glichkeiten die f�r die GameLogik vorgesehen sind:
(bei fehlenden Dingen oder Sonderw�nschen bitte fragen, NICHT SELBER RUMFUMMELN!)

kartenDeck.erstelleNeueKarte(motiv, spalte, aufgedeckt?) !	 -> Erstellt eine neue Karte des angegebenen Typs
kartenDeck.loescheKarte(id) ! 	-> L�st einen letzten flip aus, der nach Ausf�hrung automatisch die Karte l�scht (sozusagen ein Todesflipp... hrhr..)
kartenDeck.flippeKarte(id) !   -> Dreht die Karte um (in beiden Richtungen nutzbar)
kartenDeck.motivVonKarte(id) ?   -> gibt Motivnummer der Karte zur�ck
kartenDeck.existiertKarte(id) ?    -> Pr�ft ob Karte exisitert (gibt true/false zur�ck)
karten.setzeGeschwindigkeit(15) !  -> Setzt neue Fallgeschwindigkeit etc. (Standart 15)


*/
	public void generator(int delta) {
		if (generatorZehntelFrequenz != 0) generatorFuellstand += delta;
		if (generatorFuellstand >= 10000 / generatorZehntelFrequenz) {
			generatorFuellstand -= 10000 / generatorZehntelFrequenz;
			kartenDeck.erstelleNeueKarte((int) (Math.random() * freigeschalteteMotive) + 1,	(int) (Math.random() * Tetmory.MAX_ANZAHL_SPALTEN), false);	
		}
	}
	
	//init-Routine zur richtigen Verlinkung, sollte nicht ver�ndert werden!	
	public void init(KartenController kartenController, EffekteController effekteController) {
		System.out.println("Initialisiere GameLogik...");
		kartenDeck = kartenController;
		effekte = effekteController;
		generatorFuellstand = 0;
	}
}