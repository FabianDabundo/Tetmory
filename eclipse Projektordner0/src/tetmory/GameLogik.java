package tetmory;

public class GameLogik {
	private EffekteController effekte; // <--In Ruhe lassen!
	private KartenController kartenDeck; // <--In Ruhe lassen!
	private byte freigeschalteteMotive;
	private double generatorZehntelFrequenz;
	private int generatorFuellstand; // <--In Ruhe lassen!
	//Es dürfen und sollen gerne weitere Variablen & Methoden erstellt werden


	/* Wird ganz am Anfang des Spiels aufgerufen, quasi wenn der
	 * Benutzer zum ersten Mal auf Start drückt.  */
	public void start() {
		System.out.println("Bereite Level vor...");
		freigeschalteteMotive = 3;
		generatorZehntelFrequenz = 5; //1 = 1 Mal in 10 Sekunden, 5 = 5 Mal in 10 Sekunden, 10 = jede Sekunde, 0 = aus.
		
	}
	
	/* Wird im Sekundentakt aufgerufen, d.h. einmal in der Sekunde
	 * hat die GameLogik hier die Möglichkeit, das Spielgeschehen zu
	 * überblicken und ggf. einzugreifen.  
	 * Kann als Zeitmesser genutzt werden (da pro Aufruf eine Sekunde vergangen)! */
	public void denke() {
	
		
	}
	
	/* Wird aufgerufen sobald der Benutzer auf eine Karte klickt,
	   Die entsprechende KartenID wird als Paramter bereitgestellt */
	public void verarbeiteEingaben(short kartenId) {
		//effekte.spieleSound((byte)1);
		kartenDeck.flippeKarte(kartenId);
		
		
	}

/* ANLEITUNG & HILFE FÜR PROGRAMMIERER DER GAMELOGIK (z.B. Fahrbahn...) :

Hier eine Übersicht der Einflussmöglichkeiten die für die GameLogik vorgesehen sind:
(bei fehlenden Dingen oder Sonderwünschen bitte fragen, NICHT SELBER RUMFUMMELN!)

kartenDeck.erstelleNeueKarte(motiv, spalte, aufgedeckt?) !	 -> Erstellt eine neue Karte des angegebenen Typs
kartenDeck.loescheKarte(id) ! 	-> Löst einen letzten flip aus, der nach Ausführung automatisch die Karte löscht (sozusagen ein Todesflipp... hrhr..)
kartenDeck.flippeKarte(id) !   -> Dreht die Karte um (in beiden Richtungen nutzbar)
kartenDeck.motivVonKarte(id) ?   -> gibt Motivnummer der Karte zurück
kartenDeck.existiertKarte(id) ?    -> Prüft ob Karte exisitert (gibt true/false zurück)
karten.setzeGeschwindigkeit(15) !  -> Setzt neue Fallgeschwindigkeit etc. (Standart 15)


*/
	public void generator(int delta) {
		if (generatorZehntelFrequenz != 0) generatorFuellstand += delta;
		if (generatorFuellstand >= 10000 / generatorZehntelFrequenz) {
			generatorFuellstand -= 10000 / generatorZehntelFrequenz;
			kartenDeck.erstelleNeueKarte((int) (Math.random() * freigeschalteteMotive) + 1,	(int) (Math.random() * Tetmory.MAX_ANZAHL_SPALTEN), false);	
		}
	}
	
	//init-Routine zur richtigen Verlinkung, sollte nicht verändert werden!	
	public void init(KartenController kartenController, EffekteController effekteController) {
		System.out.println("Initialisiere GameLogik...");
		kartenDeck = kartenController;
		effekte = effekteController;
		generatorFuellstand = 0;
	}
}