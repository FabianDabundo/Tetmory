package tetmory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


//VORLÄUFIG, NOCH OHNE EIGENTLICHE FUNKTION
public class Grafikspeicher {
private GuiController screen;
private BufferedImage[] grafik;

	public void init(GuiController guiController) {
		System.out.println("Initialisiere Grafikspeicher...");
		screen = guiController;
		grafik = new BufferedImage[Tetmory.ANZAHL_DER_ZU_LADENDEN_GRAFIKEN];

	}
	
	//Läd alle Grafiken aus dem Grafikverzeichnis
	// unverzüglich in den Speicher
	public void ladeGrafiken() {
		System.out.println("Lade Grafiken...");
		int i; URL pfad;
		for (i=0; i<grafik.length; i++) {
			pfad = getClass().getClassLoader().getResource("grafik/" + i + ".png");
			if (pfad != null)
			try {
				grafik[i] = ImageIO.read(pfad);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
	}
	
	/*Nimmt Zeichenanfragen von anderen Klassen entgegen,
	 * verknüpft sie mit einer gespeicherten Grafik,
	 * und leitet alles an den GUI-Controller weiter. */
	public void zeichneGrafik(byte id, short x, short y, short breite, short hoehe) {
		screen.zeichneBild(grafik[id], x, y, breite, hoehe);
	}
	public void zeichneGrafik(byte id, short x, short y) {
		screen.zeichneBild(grafik[id], x, y, (short) grafik[id].getWidth(), (short) grafik[id].getHeight());
	}

}
