package tetmory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


//Puffert sämtliche Zeichenbefehle bis zur endgültigen Ausgabe
public class GuiController {
	Gui gui;
	private Image buffer;
	private Graphics buffG;
	
	public GuiController() {
		System.out.println("Erstelle Gui...");
		 gui = new Gui();
	}
	
	public void init() {
		gui.init();
		try	{ 
			//(Warten (aus irgendeinem Grund kommt Windows sonst mit der Anzeige nicht hinterher...)
				Thread.sleep (100);
			} catch (InterruptedException ex){};
		System.out.println("Initialisiere GuiController...");
		buffer = gui.gibRenderPanelzurueck().createImage(Tetmory.PANELBREITE,Tetmory.PANELHOEHE);
		buffG = buffer.getGraphics();
		buffG.setColor(new Color(60,25,25));
		buffG.fillRect(0,0, Tetmory.PANELBREITE, Tetmory.PANELHOEHE);
		buffG.setColor(new Color(240,240,240));
		buffG.drawString("Tetmory wird geladen...", 175, Tetmory.PANELHOEHE / 2 + 8);
		buffG.setColor(new Color(60,25,25));
		allesRendern();
	}
	 public JFrame gibFensterzurueck()  {
		 return gui.gibFensterzurueck();
	 }
	 public Renderflaeche gibRenderPanelzurueck()  {
		 return gui.gibRenderPanelzurueck();
	 }
	
	// Zeichnet ein Element (auf den Zwischenpuffer)
	public void zeichneFarbe(Color color, short x, short y, short breite, short hoehe) {
		buffG.setColor(color);
		buffG.fillRect(x, y, breite, hoehe);
	}
	public void zeichneBild(BufferedImage bild, short x, short y, short breite, short hoehe) {
		buffG.drawImage(bild, x, y, breite, hoehe, null);
	}
	
	//Rendert alles auf das Fenster und leert den Puffer
	public void allesRendern() {
		gui.gibRenderPanelzurueck().render(buffer);
		buffG.setColor(new Color(60,25,25));
		buffG.fillRect(0,0, Tetmory.PANELBREITE, Tetmory.PANELHOEHE);
		buffG.setColor(new Color(240,240,240));
		buffG.drawString("Tetmory läuft.  :)", 175, Tetmory.PANELHOEHE / 2 + 8);
	}

	
}
