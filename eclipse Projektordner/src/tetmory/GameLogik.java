package tetmory;

public class GameLogik {
	private EffekteController effekte; // <--In Ruhe lassen!
	private KartenController kartenDeck; // <--In Ruhe lassen!
	private byte freigeschalteteMotive;
	private double generatorZehntelFrequenz;
	private int generatorFuellstand; // <--In Ruhe lassen!
	private short offeneKartenAnzahl; // Variable, die Anzahl der aufgedeckten Karten angibt
	private short offeneKartenIds[] = new short[2]; //Array der die Id's der aufgedeckten Karten speichert
	private short vergleichsVariable; // Speichert MotivId. Zum Vergleichen von MotivId's
	private short level; // <== DAS LEVEL!!!
	private byte paare;
	private int highscore;
	private short levelUpMultiplikator = 4, levelUpSummand = 5; //bestimmen ab welcher Paarzahl ein levelUp() erfolgt
	private short highscorePunkte = 50;
	//Es dürfen und sollen gerne weitere Variablen & Methoden erstellt werden


	/* Wird ganz am Anfang des Spiels aufgerufen, quasi wenn der
	 * Benutzer zum ersten Mal auf Start drückt.  */
	public void start() {
		System.out.println("Bereite Level vor...");
		level = 0;
		paare = 0;
		highscore = 0;
		freigeschalteteMotive = 4;
		
		offeneKartenAnzahl = 0;
		offeneKartenIds[0] = -1;
		offeneKartenIds[1] = -1;
		//generatorZehntelFrequenz = 
		frequenzErhoehen(); //1 = 1 Mal in 10 Sekunden, 5 = 5 Mal in 10 Sekunden, 10 = jede Sekunde, 0 = aus.
		levelUp();
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
		/* ist die Karte noch nicht aufgedeckt, tritt der if-Block ein.
		 * hier werden die Id's der aufgedeckten Karten gespeichert und die Motive miteinander verglichen.
		 * wird eine bereits gespeicherte Id, also aufgedeckte Karte angeklickt, tritt der else-Block in Kraft.
		 * hier wird die erneut angeklickte Karte wieder zugedeckt, ihre Id also ersetzt (gelöscht).
		 * ist noch eine Karte aufgedeckt, wird ihr Motiv zum neuen VergleichsMotiv								*/
		
		/************* if-Block **************/
		if ((offeneKartenAnzahl < 3) && kartenId != offeneKartenIds[0] && kartenId != offeneKartenIds[1]) {
			//effekte.spieleSound((byte)1);     //Audio-Effekt
			kartenDeck.flippeKarte(kartenId); //Visual-Effekt
			
			offeneKartenAnzahl += 1;
			
			//Fall: eine Karte wird aufgedeckt. Ist eine Karte aufgedeckt, wird ihre MotivId gespeichert, da ihr Motiv mit dem der nächsten aufgedeckten Karte verglichen	werden muss	
			if (offeneKartenAnzahl == 1) { 
				offeneKartenIds[0] = kartenId;				
				vergleichsVariable = kartenDeck.motivVonKarte(kartenId);
			}
			
			//Fall: Der KILLERFLIP!!!
			if (offeneKartenAnzahl == 2) {
				if (vergleichsVariable == kartenDeck.motivVonKarte(kartenId)) {
					if (offeneKartenIds[0] == -1)
						kartenDeck.loescheKarte(offeneKartenIds[1]);
					else kartenDeck.loescheKarte(offeneKartenIds[0]);
					kartenDeck.loescheKarte(kartenId);
					offeneKartenAnzahl = 0;
					offeneKartenIds[0] = -1;
					offeneKartenIds[1] = -1;
					paareErhoehen();
					highscoreErhoehen();
				}
			//Fall: Wenn die Motive nicht übereinstimmen
				else {
					if (offeneKartenIds[0] == -1)
						offeneKartenIds[0] = kartenId;
					else offeneKartenIds[1] = kartenId;
				}
			}
			
			// Fall: Zwei Karten sind bereits aufgedeckt, eine dritte wird aufgedeckt => die anderen beiden Karten werden wieder verdeckt
			if (offeneKartenAnzahl == 3) {
				kartenDeck.flippeKarte(offeneKartenIds[0]);
				kartenDeck.flippeKarte(offeneKartenIds[1]);
				offeneKartenAnzahl = 1;
				offeneKartenIds[0] = kartenId;
				offeneKartenIds[1] = -1;
				vergleichsVariable = kartenDeck.motivVonKarte(kartenId);
			}
		}
		
		/************* else-Block **************/
		else {
			if (kartenId == offeneKartenIds[0]) {
				offeneKartenIds[0] = -1;
				if (offeneKartenAnzahl == 2)
					vergleichsVariable = kartenDeck.motivVonKarte(offeneKartenIds[1]);
			}
			
			else {
				offeneKartenIds[1] = -1;
				if (offeneKartenAnzahl == 2)
					vergleichsVariable = kartenDeck.motivVonKarte(offeneKartenIds[0]);
			}
			
			kartenDeck.flippeKarte(kartenId);
			offeneKartenAnzahl -= 1;
		}
		
	}

/* ANLEITUNG & HILFE FÜR PROGRAMMIERER DER GAMELOGIK (z.B. Fahrbahn...) :

Hier eine Übersicht der Einflussmöglichkeiten die für die GameLogik vorgesehen sind:
(bei fehlenden Dingen oder Sonderwünschen bitte fragen, NICHT SELBER RUMFUMMELN!)

kartenDeck.erstelleNeueKarte(motiv, spalte, aufgedeckt?) !	 -> Erstellt eine neue Karte des angegebenen Typs
kartenDeck.loescheKarte(id) ! 	-> Löst einen letzten flip aus, der nach Ausführung automatisch die Karte löscht (sozusagen ein Todesflipp... hrhr..)
kartenDeck.flippeKarte(id) !   -> Dreht die Karte um (in beiden Richtungen nutzbar)
kartenDeck.motivVonKarte(id) ?   -> gibt Motivnummer der Karte zurück
kartenDeck.existiertKarte(id) ?    -> Prüft ob Karte exisitert (gibt true/false zurück)
kartenDeck.setzeGeschwindigkeit(15) !  -> Setzt neue Fallgeschwindigkeit etc. (Standart 15)


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
	
	
	
/* *************************************************************************************************************************************************************** */
	
	//generatorZehntelFrequenz-Methode zum Beeinflussen
	public void frequenzErhoehen () {
		generatorZehntelFrequenz = level+4;
	}
		
	///*** Levelmethoden ***\\\
	public short getLevel() {
					return level;				
	}
	public void changeLevel(short x) {
		level = x;
	}
	public void levelUp() {
		int i;
		
		paare = 0;
		level += 1;
		if (level % 2 == 0)
			frequenzErhoehen();
		kartenDeck.setzeGeschwindigkeit(getLevel()+14);
		//if(freigeschalteteMotive < 6 && (level-1) % 2 == 0)
			//freigeschalteteMotive += 1;
		for (i=0; i<Tetmory.MAX_ANZAHL_SPALTEN; i++) {
			kartenDeck.erstelleNeueKarte((int)(Math.random() * freigeschalteteMotive) + 1,(int) i, false);
		}
		
		System.out.println("LevelUp");
	}
	//gesammelte Paare
	public void paareErhoehen() {
		paare += 1;
		if (paare == (levelUpMultiplikator * level + levelUpSummand))
			levelUp();
	}
	//Highscoremethoden
	public int getHighscore() {
		return highscore;
	}
	public void highscoreErhoehen() {
		highscore += (highscorePunkte + level * highscorePunkte);
		System.out.println(highscore);
	}
}

