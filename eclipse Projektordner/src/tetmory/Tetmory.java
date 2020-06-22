package tetmory;

//Die Controllerklasse, sorgt für geordneten Spielverlauf
public class Tetmory implements Runnable {
	Grafikspeicher videoStore;
	GuiController screen;
	EingabenVerwaltung input;
	KartenController karten;
	EffekteController effekt;
	GameLogik brain;
	//Spielkonstanten
	public static final short FENSTERBREITE = 450;
	public static final short FENSTERHOEHE = 725;
	public static final short PANELBREITE = 450;
	public static final short PANELHOEHE = 697;
	public static boolean PAUSE = false;
	public static short TOASTBREITE = 75;
	public static short TOASTHOEHE = 75;
	public static short TOASTSPALTENABSTAND = 16;
	public static short TOASTZEILENABSTAND = 15;
	public static final byte MAX_ANZAHL_SPALTEN = 5;
	public static final short MAXIMALE_ANZAHL_GLEICHZEITIG_DARGESTELLTER_KARTEN = 50;
	public static final byte ANZAHL_DER_ZU_LADENDEN_GRAFIKEN = 10; //Maximalwert, muss nicht erreicht werden
	public static final byte ANZAHL_DER_ZU_LADENDEN_SOUNDS = 2; //Maximalwert, muss nicht erreicht werden
	
	
	//Initialisieren sämtlicher Komponenten
	private void init() {
		System.out.println("Spielcontroller erstellt, erstelle Objekte...");
		System.out.println("Erstelle GuiController...");
		screen = new GuiController();
		System.out.println("Erstelle Grafikspeicher...");
		videoStore = new Grafikspeicher();
		System.out.println("Erstelle Eingabenverwaltung...");
		input = new EingabenVerwaltung();
		System.out.println("Erstelle Kartencontroller...");
		karten = new KartenController();
		System.out.println("Erstelle EffektController...");
		effekt = new EffekteController();
		System.out.println("Erstelle GameLogik...");
		brain = new GameLogik();
		//Alle Objekte wurden erstellt, jetzt werden sie initialisiert
		screen.init();
		videoStore.init(screen);
		karten.init(videoStore);
		effekt.init(videoStore);
		brain.init(karten, effekt);
		input.init(brain, karten,screen.gui.gibRenderPanelzurueck(), screen.gibFensterzurueck());
		System.out.println("Tetmory wurde initialisiert.");
	}
	
	//Laden der Daten für das Spiel
	private void load() {
		//Spiel wird vorbereitet
		System.out.println("Lade neues Spiel...");
		videoStore.ladeGrafiken();
		effekt.ladeSounds();
		brain.start();
		System.out.println("Bereit zum Loslegen.");
	}
	
	//Das Spiel ab dem ersten Level
	private void game() {
		System.out.println("Beginne Game-Loop...");
		int delta; //<--enthält immer die Zeit in ms seit der letzten Runde
		int braintime = 0; //bei jeder 1000 darf die GameLogik einmal denken
		long letzterLoop = System.currentTimeMillis();
		while (true) {
			delta = (int)(System.currentTimeMillis() - letzterLoop);
			letzterLoop = System.currentTimeMillis();
			braintime += delta;
		//..................................................
			/*Game-Loop..........................................................................
			  Alles was hier drin steht wird regelmäßig ausgeführt (rundenweise).
			  Die Reihenfolge sollte so viel Sinn wie möglich machen,
			  und der Code so effizient sein wie möglich!
			  */

			if (!PAUSE) {
				input.verarbeiteEingaben();
				if (braintime >= 1000) {
					braintime -= 1000;
					brain.denke();
				}
				brain.generator(delta);
			}
			karten.aktualisiereKarten(delta);
			karten.zeichneAlleKarten();
			screen.allesRendern((int)brain.getHighscore(),brain.getLevel());


			//Runde vorbei.......................................................................
		//..................................................
			try	{ 
			//(Gibt Prozessor bis zur nächsten Runde frei)
				Thread.sleep (10);
			} catch (InterruptedException ex){};
		}
	}
	

	//Das eigentliche Spiel, von Anfang bis Ende :)
	public void run() {
		init();
		load();
		game();
	}

	//Nur zum Starten nötig, es geht bei "public void run()" weiter.
	public static void main(String[] args) {
		System.out.println("Starte Tetmory...");
		Thread tetmory = new Thread (new Tetmory(), "Tetmory");
		tetmory.start ();
		System.out.println("Thread erstellt.");
	}
}
