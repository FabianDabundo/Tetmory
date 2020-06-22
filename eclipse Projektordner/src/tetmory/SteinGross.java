package tetmory;

public class SteinGross extends Stein {
	public static short TOASTBREITE = (short) (Tetmory.TOASTBREITE * 2);
	public static short TOASTHOEHE = (short) (Tetmory.TOASTHOEHE * 2);
	private Stein karteRechtsUnter;
	private Stein karteRechtsUeber;
	
	public SteinGross (byte motiv, byte spalte, boolean aufgedeckt) {
		super(motiv, spalte, aufgedeckt);
		karteRechtsUnter = null;
		karteRechtsUeber = null;
		y = y + Stein.TOASTHOEHE - TOASTHOEHE;
	}
	
	public void registriereDich(Stein drunter, Stein drunterRechts) {
		super.registriereDich(drunter);
		karteRechtsUnter = drunterRechts;
		if (drunterRechts != null)
			drunterRechts.setzeKarteUeber(this, (byte) (spalte + 1));		
	}
	
	public void verabschiedeDich() {
		super.verabschiedeDich();
		if (karteRechtsUeber != null) 
			karteRechtsUeber.setzeKarteUnter(karteRechtsUnter, (byte)(spalte + 1));
		if (karteRechtsUnter != null)
			karteRechtsUnter.setzeKarteUeber(karteRechtsUeber, (byte)(spalte + 1));
	}
	
	//Kapsel
	short getHoehe() {
		return TOASTHOEHE;
	}
	short getBreite() {
		return TOASTBREITE;
	}
	public void setzeKarteUeber(Stein karteUeber, byte spalte) {
		if (spalte == this.spalte)
			super.setzeKarteUeber(karteUeber, spalte);
		else karteRechtsUeber = karteUeber;
	}
	public void setzeKarteUnter(Stein karteUnter, byte spalte) {
		if (spalte == this.spalte)
			super.setzeKarteUnter(karteUnter, spalte);
		else karteRechtsUnter = karteUnter;
	}
	public Stein holeKarteRechtsUnter() {
		return karteRechtsUnter;
	}
	

	short flipBreite() {
		return (short) (super.flipBreite() * 2);
	}
	
	boolean darfIchMichUeberhauptBewegen(boolean sollKorrigieren) {
		if (karteUnter != null) {
			if (karteUnter.y - y <= TOASTHOEHE + Tetmory.TOASTZEILENABSTAND)
				return false;
			if (karteRechtsUnter != null) { 
				if (karteRechtsUnter.y - y <= TOASTHOEHE + Tetmory.TOASTZEILENABSTAND)	return false; 
				return true;
			} return true;
		}
		if (karteRechtsUnter != null)
			if (karteRechtsUnter.y - y > TOASTHOEHE + Tetmory.TOASTZEILENABSTAND) return true;
			else return false;
		if (Tetmory.PANELHOEHE - y > TOASTHOEHE ) return true;
		else {
			if (sollKorrigieren)
				y = Tetmory.PANELHOEHE - TOASTHOEHE;
			return false;
		}	
	}
	
	public void bewegDich(int zeit) {
		super.bewegDich(zeit / 2);
	}
	
	public boolean flippeDich(int zeit) {
		return super.flippeDich(zeit / 4);
	}
}
