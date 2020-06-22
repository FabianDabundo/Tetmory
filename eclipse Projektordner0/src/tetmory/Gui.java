package tetmory;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;


public class Gui {
	private JFrame fenster; 
	private Renderflaeche steinfeld;
	
	Gui() {
		fenster = new JFrame("Tetmory");
		steinfeld = new Renderflaeche();
	}
	
	 public void init() {
		System.out.println("Initialisiere Gui...");
		fenster.setSize(Tetmory.FENSTERBREITE,Tetmory.FENSTERHOEHE);
		fenster.setResizable(false);
		fenster.setLocationRelativeTo(null);
		fenster.setVisible(true);
		fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fenster.add(steinfeld);
		steinfeld.init();	
	}
	 public JFrame gibFensterzurueck()  {
		 return fenster;
	 }
	 public Renderflaeche gibRenderPanelzurueck()  {
		 return steinfeld;
	 }
	
	
}



//Die eigentliche Spielfläche mit den Steinen. (Nur zum Rendern da)
class Renderflaeche extends JPanel {
	Graphics g;
	
	public void init() {
		this.setDoubleBuffered(false);
		this.setIgnoreRepaint(true);
	}
	
	public void render(Image bild) {
		g = this.getGraphics();
		g.drawImage(bild, 0,0,this);
	}
	
	//Normale Zeichenmethode wird hier überschrieben so dass sie nichts mehr macht
	public void paintComponent(Graphics g) {
		
	}
}