
package sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.URL;

public class Sound extends Thread {
	private Player soundPlayer;
	private String soundPath;
	private BufferedInputStream soundFile;
	
	public Sound( String name ) {
		InputStream file = Sound.class.getResourceAsStream( "/assets/sound/" + name + ".mp3" );
		soundFile = new BufferedInputStream( file );
		URL url = Sound.class.getResource( "/assets/sound/" + name + ".mp3");
		soundPath = url.getPath().replaceAll("%20", " ");
	}
	
	public void play() {
		if (!isAlive())
			start();
	}
	
	public void run() {
		try {
			//FileInputStream fis = new FileInputStream( soundPath );
			//BufferedInputStream bis = new BufferedInputStream( soundFile );
			soundPlayer = new Player( soundFile );
			soundPlayer.play();

		} catch (JavaLayerException ex) {
			System.out.println( ex.getMessage() );
		}
	}
}
