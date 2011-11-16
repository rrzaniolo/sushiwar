
package sound;

import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
import java.net.URL;

public class Music extends Thread {
	private Player musicPlayer;
	private String musicPath;
	private boolean repeat;
	
	public Music( String name ) {
		URL url = Music.class.getResource( "/assets/music/" + name + ".mp3");
		musicPath = url.getPath().replaceAll("%20", " ");
		repeat = false;
	}
	
	public void play() {
		repeat = true;
		start();
	}
	
	public void playOnce() {
		repeat = false;
		start();
	}
	
	public void run() {
		BufferedInputStream bis = null;
		
		do {
			try {
				FileInputStream fis = new FileInputStream( musicPath );
				bis = new BufferedInputStream( fis );
				musicPlayer = new Player( bis );
				musicPlayer.play();
				
			} catch (FileNotFoundException ex) {
				System.out.println( ex.getMessage() );
			} catch (JavaLayerException ex) {
				System.out.println( ex.getMessage() );
			}
			
		} while (repeat);
	}
}
