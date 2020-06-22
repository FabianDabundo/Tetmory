package tetmory;

public class Stein {
	public static byte max_speed = 15; 
	public static float beschleunigung = 3;
	public static float flipdauer = 225;
	public float y;
	public byte spalte;
	public byte motiv;
	public boolean verdeckt;
	private float speed;
	private short flipzahl;
	private byte fliprichtung;
	
	private Stein karteUnter;
	private Stein karteUeber;
	
	public Stein(byte motiv, byte spalte, boolean aufgedeckt) {
		this.motiv = motiv;
		this.spalte = spalte;
		y = -20 - Tetmory.TOASTHOEHE;
		speed = 0;
		flipzahl = 0;
		fliprichtung = 0;
		verdeckt = !aufgedeckt;
		karteUnter = null;
		karteUeber = null;
	}
	
	//Kapselung der Variablen
	public void setzeKarteUnter(Stein karteUnter) {
		this.karteUnter = karteUnter;
	}
	public Stein holeKarteUnter() {
		return karteUnter;
	}
	public void setzeKarteUeber(Stein karteUeber) {
		this.karteUeber = karteUeber;
	}
	
	/* Stellt Weichen für einen normalen Flip.
	 * Je nach Sichtbarkeit der Karte entscheidet sich 
	 * hier in welche Richtung geflippt wird. */
	public void starteFlip() {
	if (verdeckt) fliprichtung = 1;
	else fliprichtung = -1;
	 }
	
	/* Startet einen tödlichen killFlip der Richtung "2".
	 * Der killFlip ist die offizielle Methode Karten zu löschen
	 */
	public void killFlip() {
		fliprichtung = 2;
	}
	
	/* Errechnet die aktuelle Kartenbreite abhängig vom Fortschritt
	 * des Flip-Vorgangs. Also Grundlage dient die "flipzahl"-Variable.
	 */
	public short flipBreite() {
		if (fliprichtung==0) return Tetmory.TOASTBREITE;
		else {
			short breite;
			breite = (short)(Math.cos(Math.PI*flipzahl/flipdauer)*Tetmory.TOASTBREITE);
			if (breite < 0) breite *= -1;
			return breite;
		}
	}
	
	/* Überprüft ob die Karte noch PLatz zum Fallen hat oder bereits auf
	 * etwas anderem (Anderer Stein/ Boden) aufliegt. Korrigiert bei Bedarf
	   auch die zu weit zurückgelegte Strecke. */
	private boolean darfIchMichUeberhauptBewegen(boolean sollKorrigieren) {
		if (karteUnter != null) {
			if (karteUnter.y - y > Tetmory.TOASTHOEHE + Tetmory.TOASTZEILENABSTAND) return true;
			else {
				if (sollKorrigieren)
				;
				return false;
			}
		}  else {
			if (Tetmory.PANELHOEHE - y > Tetmory.TOASTHOEHE ) return true;
			else {
				if (sollKorrigieren)
					y = Tetmory.PANELHOEHE - Tetmory.TOASTHOEHE;
				return false;
			}
		}
	}
	
	//Berechnet die nötige Bewegung durch die Schwerkraft (Variable: Beschleunigung)
	public void bewegDich(int zeit) {
		if (darfIchMichUeberhauptBewegen(false)) {
			if (speed != max_speed)
				if (speed < max_speed) 
					speed += beschleunigung * zeit / 100;
				else {
					speed -= beschleunigung * zeit / 100;
					if (speed < max_speed) speed = max_speed;
				}
			y += speed * zeit / 100;
			darfIchMichUeberhauptBewegen(true);
		} 
	}
	
	/* Führt den Flipp durch.
	 * Abhängig davon welche Flipprichtung gerade eingeschlagen ist,
	 * ergeben sich andere Eigenschaften */
	public boolean flippeDich(int zeit) {
		if (fliprichtung != 0) {
			flipzahl += zeit;
			if (flipzahl >= flipdauer / 2 && flipzahl - zeit < flipdauer / 2)
				switch (fliprichtung) {
				case 1: // Die Karte war verdeckt und wird jetzt (GERADE) sichtbar
					verdeckt = false;
				break;
				case -1: // Andersrum
					verdeckt = true;
				break;
				case 2: // Die Karte macht einen Killflip. Dieser dauert etwas länger und endet...
					verdeckt = !verdeckt;
				break;
				case -2: //...hier! Die Karte hat eineinhalb Umdrehungen gemacht und kann (JETZT) gelöscht werden.
					if (karteUeber != null) 
						karteUeber.karteUnter = karteUnter;
					if (karteUnter != null)
						karteUnter.karteUeber = karteUeber;
					return false;
					
				}
			if (flipzahl >= flipdauer) { //Flip beendet
				flipzahl = 0;
				if (fliprichtung != 2) //bzw. bei Killflip: Noch eine halbe Drehung zurück, dann Sense
					fliprichtung = 0;
				else fliprichtung = -2;
				
			}
				
		}
		return true; //True immer dann wenn die Karte überleben soll
	}
	
}
