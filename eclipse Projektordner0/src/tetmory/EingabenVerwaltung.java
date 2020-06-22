package tetmory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

//Noch zu TESTZWECKEN. Noch nicht ausreichend implementiert
public class EingabenVerwaltung implements MouseListener, KeyListener {
	private GameLogik brain;
	private KartenController karten;
	private short letzteGeklickteKarte = -1;
	private byte vorherigerSpeed;
	
	public void init(GameLogik gameLogik, KartenController kartenController, Renderflaeche renderflaeche, JFrame fenster) {
		System.out.println("Initialisiere EingabenVerwaltung");
		brain = gameLogik;
		karten = kartenController;
		renderflaeche.addMouseListener(this);	
		fenster.addKeyListener(this);
	}

	public void verarbeiteEingaben() {
		if (letzteGeklickteKarte != -1) { 
			brain.verarbeiteEingaben(letzteGeklickteKarte);
			letzteGeklickteKarte = -1;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (!Tetmory.PAUSE)
			letzteGeklickteKarte = karten.sucheNachKollidierenderKarte(e.getX(), e.getY());
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}


	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (Stein.max_speed == 0) {
				Stein.max_speed = vorherigerSpeed;
				Tetmory.PAUSE = false;
			} 
			else {
				vorherigerSpeed = Stein.max_speed;
				Stein.max_speed = 0;
				Tetmory.PAUSE = true;
			}
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
