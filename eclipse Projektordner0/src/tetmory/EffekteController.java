package tetmory;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;



//Noch nicht implementiert
public class EffekteController {
	private Grafikspeicher videoStore;
	private AudioClip[] audio;
	
	public void init(Grafikspeicher grafikspeicher) {
		System.out.println("Initialisiere Effektcontroller...");
		videoStore = grafikspeicher;
		audio = new AudioClip[Tetmory.ANZAHL_DER_ZU_LADENDEN_SOUNDS];
	}
	
	public void ladeSounds()  {
		System.out.println("Lade Sounds...");
		int i; URL pfad;
		for (i=0; i<audio.length; i++) {
			pfad = getClass().getClassLoader().getResource("sound/" + i + ".wav");
			if (pfad != null)
			audio[i] = Applet.newAudioClip(pfad);
		}
		
		//audio[0].loop();
	}

	public void spieleSound(byte nummer) {
		audio[nummer].play();
	}
}
