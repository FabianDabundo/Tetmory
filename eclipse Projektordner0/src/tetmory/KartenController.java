package tetmory;


public class KartenController {
	private Grafikspeicher videoStore;
	private Stein[] karte;
	private Stein[] obersteKarteInSpalte;
	
	public void init(Grafikspeicher grafikspeicher) {
		System.out.println("Initialisiere Kartencontroller...");
		videoStore = grafikspeicher;
		karte = new Stein[Tetmory.MAXIMALE_ANZAHL_GLEICHZEITIG_DARGESTELLTER_KARTEN];
		obersteKarteInSpalte = new Stein[Tetmory.MAX_ANZAHL_SPALTEN];
	}
	
	public void setzeGeschwindigkeit(int wert) {
		Stein.max_speed = (byte) wert;
		Stein.beschleunigung = wert / 5;
		Stein.flipdauer = 3375 / wert;		
	}
	
	/*Erstellt neue Karte und f�gt sie dem Objektarray (siehe oben) hinzu
	  Merkt sich welche Karte in einer Spalte jeweils oben liegt, 
	  Teilt der neuen Karte mit welche unter ihr liegt und andersrum. */
	public void erstelleNeueKarte(int motiv, int spalte, boolean aufgedeckt) {
		short i;
		for (i = 0; i < karte.length-1; i++) 
			if (karte[i]==null) {
				karte[i]= new Stein((byte)motiv, (byte)spalte, aufgedeckt);
				karte[i].setzeKarteUnter(obersteKarteInSpalte[spalte]);
				if (obersteKarteInSpalte[spalte] != null)
					obersteKarteInSpalte[spalte].setzeKarteUeber(karte[i]);
				obersteKarteInSpalte[spalte]=karte[i];
				break;
			}
	}
	
	
	
	//L�st einen flip mit der fliprichtung "2" aus, der nach Ausf�hrung die Karte l�scht (siehe unten)
	public void loescheKarte(short id) {
			karte[id].killFlip();
	}
	
	//L�st einen normalen Kartenflip aus (in beiden Richtungen nutzbar)
	public void flippeKarte(short id) {
			karte[id].starteFlip();
	}
	
	//gibt Motivnummer der Karte zur�ck
	public short motivVonKarte(byte id) {
		return karte[id].motiv;
	}
	
	//Pr�ft ob Karte exisitert
	public boolean existiertKarte(short id) {
		if (karte[id] == null) return false;
		else return true;
	}
	
	//�berpr�ft s�mtliche vorhandenen Karten auf Kollision mit den �bergebenen Parameterkoordinaten
	public short sucheNachKollidierenderKarte(int x, int y) {
		int i;
		for (i = 0; i < karte.length; i++) {
			if (karte[i]!=null) {
				if (karte[i].y < y && karte[i].y + Tetmory.TOASTHOEHE > y)
					if (karte[i].spalte * (Tetmory.TOASTBREITE + Tetmory.TOASTSPALTENABSTAND) < x)
						if (karte[i].spalte * (Tetmory.TOASTBREITE + Tetmory.TOASTSPALTENABSTAND) + Tetmory.TOASTBREITE > x)
							return (short) i;
			}
		}
		return -1;
	}
	
	/*L�sst jede Karte ihre Positions�nderung sowie Drehung berechnen.
	  Sorgt also f�r die Animation und Bewegung.
	  Desweiteren werden hier Karten ENDG�LTIG gel�scht*/
	public void aktualisiereKarten(int zeit) {
		short i;
		for (i = 0; i < karte.length; i++)
			if (karte[i]!=null) {
				karte[i].bewegDich(zeit);
				//-------Achtung!-----------------------
				if (karte[i].flippeDich(zeit) == false) {
					/* Sollte "flippeDich" wie in diesem Fall "false" zur�ckliefern, handelte es sich um einen 
					Flip mit Richtung "2", der gerade abgeschlossen wurde (Karte nicht mehr sichtbar).
					Sie wird deswegen nun hier endg�ltig gel�scht. */ 
					if (karte[i] == obersteKarteInSpalte[karte[i].spalte])
						obersteKarteInSpalte[karte[i].spalte] = karte[i].holeKarteUnter();
					karte[i] = null;
				}
			}
	}
	
	/*Gibt Anweisungen an den Grafikspeicher, s�mtliche Karten zu zeichnen,
	  und berechnet die hierf�r notwendigen Parameter.*/
	public void zeichneAlleKarten() {
		short i, x, y, breite;
		byte motiv;
		for (i = 0; i < karte.length; i++) 
			if (karte[i]!=null) {
				y = (short) karte[i].y;
				x = (short) (karte[i].spalte * (Tetmory.TOASTBREITE + Tetmory.TOASTSPALTENABSTAND));
				if (karte[i].verdeckt) motiv = 0;
				else motiv = karte[i].motiv;
				breite = karte[i].flipBreite();
				if (breite != Tetmory.TOASTBREITE) 
					x += (Tetmory.TOASTBREITE - breite) / 2;
				videoStore.zeichneGrafik(motiv, x, y, breite, Tetmory.TOASTHOEHE);
			}
				
	}
	
}
