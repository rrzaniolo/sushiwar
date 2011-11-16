
package sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.URL;

public class Sound extends Thread {
	private Player soundPlayer;
	private String soundPath;
	
	public Sound( String name ) {
		URL url = Sound.class.getResource( "/assets/sound/" + name + ".mp3");
		soundPath = url.getPath().replaceAll("%20", " ");
	}
	
	public void play() {
		if (!isAlive())
			start();
	}
	
	public void run() {
		try {
			FileInputStream fis = new FileInputStream( soundPath );
			BufferedInputStream bis = new BufferedInputStream( fis );
			soundPlayer = new Player( bis );
			soundPlayer.play();

		} catch (FileNotFoundException ex) {
			System.out.println( ex.getMessage() );
		} catch (JavaLayerException ex) {
			System.out.println( ex.getMessage() );
		}
	}
}
