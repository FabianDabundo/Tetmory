package tetmory;

public class Stein {
	public static short TOASTBREITE = Tetmory.TOASTBREITE;
	public static short TOASTHOEHE = Tetmory.TOASTHOEHE;
	public static byte max_speed = 15; 
	public static float beschleunigung = 3;
	public static float flipdauer = 225;
	public float y;
	public byte spalte;
	public byte motiv;
	public boolean verdeckt;
	private float speed;
	private short flipzahl;
	private short fliprichtung;
	Stein karteUnter;
	Stein karteUeber;
	
	public Stein(byte motiv, byte spalte, boolean aufgedeckt) {
		this.motiv = motiv;
		this.spalte = spalte;
		y = -20 - TOASTHOEHE;
		speed = 0;
		flipzahl = 0;
		fliprichtung = 0;
		verdeckt = !aufgedeckt;
		karteUnter = null;
		karteUeber = null;
	}
	
	// Meldet sich beim Stein drunter als neuer Stein drüber an.
	public void registriereDich(Stein drunter) {
		karteUnter = drunter;
		if (drunter != null)
			drunter.setzeKarteUeber(this, spalte);
	}
	
	// Verlinkt falls vorhanden die Steine über und unter sich miteinander
	public void verabschiedeDich() {
		if (karteUeber != null) 
			karteUeber.setzeKarteUnter(karteUnter, spalte);
		if (karteUnter != null)
			karteUnter.setzeKarteUeber(karteUeber, spalte);
	}
	
	//Kapselung der Variablen
	short getHoehe() {
		return TOASTHOEHE;
	}
	short getBreite() {
		return TOASTBREITE;
	}
	public void setzeKarteUnter(Stein karteUnter, byte spalte) {
		this.karteUnter = karteUnter;
	}
	public Stein holeKarteUnter() {
		return karteUnter;
	}
	public void setzeKarteUeber(Stein karteUeber, byte spalte) {
		this.karteUeber = karteUeber;
	}
	
	//Überprüft ob die Karte nicht grade am Killflippen ist (was schnell zu NullPointern führt..)
	boolean lebstDuNoch() {
		if (fliprichtung != 0)
			if (fliprichtung >= 32000 || fliprichtung <= -32000)
				return false;
		return true;
	}
	
	/* Stellt Weichen für einen normalen Flip.
	 * Je nach Sichtbarkeit der Karte entscheidet sich 
	 * hier in welche Richtung geflippt wird. */
	public void starteFlip() {
	if (fliprichtung == 0)	//Stein in Ruhe
		if (verdeckt) fliprichtung = 1;
		else fliprichtung = -1;
	else //Stein flippt schon
		if (fliprichtung > 0) {
			if (fliprichtung < 32000) // Nicht im Todesflip?
				fliprichtung++;
		}
		else if (fliprichtung < 0)
			if (fliprichtung > -32000)  // Nicht im Todesflip?
				fliprichtung--;
	}
	
	/* Startet einen tödlichen killFlip der Richtung "2".
	 * Der killFlip ist die offizielle Methode Karten zu löschen
	 */
	public void killFlip() {
		if (verdeckt) fliprichtung = 32001;
		else fliprichtung = -32001;
	}
	
	/* Errechnet die aktuelle Kartenbreite abhängig vom Fortschritt
	 * des Flip-Vorgangs. Also Grundlage dient die "flipzahl"-Variable.
	 */
	short flipBreite() {
		if (fliprichtung==0) return TOASTBREITE;
		else {
			short breite;
			breite = (short)(Math.cos(Math.PI*flipzahl/flipdauer)*TOASTBREITE);
			if (breite < 0) breite *= -1;
			return breite;
		}
	}
	
	/* Überprüft ob die Karte noch PLatz zum Fallen hat oder bereits auf
	 * etwas anderem (Anderer Stein/ Boden) aufliegt. Korrigiert bei Bedarf
	   auch die zu weit zurückgelegte Strecke. */
	boolean darfIchMichUeberhauptBewegen(boolean sollKorrigieren) {
		if (karteUnter != null) {
			if (karteUnter.y - y > TOASTHOEHE + Tetmory.TOASTZEILENABSTAND) return true;
			else return false;
		}  else {
			if (Tetmory.PANELHOEHE - y > TOASTHOEHE ) return true;
			else {
				if (sollKorrigieren)
					y = Tetmory.PANELHOEHE - TOASTHOEHE;
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
			if (flipzahl >= flipdauer / 2 && flipzahl - zeit < flipdauer / 2) {
				if (fliprichtung > 0) 	// Die Karte war verdeckt und wird jetzt (GERADE) sichtbar 
					verdeckt = false;
				else // Andersrum
					verdeckt = true;
				if (fliprichtung == 32000 || fliprichtung == -32000) //Killflip
					return false;
			}
			if (flipzahl >= flipdauer) { //Flip beendet
				flipzahl = 0;
				fliprichtung *= -1; // fliprichtung umpolen
				if (fliprichtung > 0) fliprichtung--; // 1 Flip abziehen (falls mehrere in der Warteschlange)
				else fliprichtung ++;
			}
				
		}
		return true; //True immer dann wenn die Karte überleben soll
	}
	
}
