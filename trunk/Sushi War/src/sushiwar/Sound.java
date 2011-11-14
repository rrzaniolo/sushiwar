
package sushiwar;


import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.*;
    
public class Sound extends Thread{
    private FileInputStream fls = null;
    private Player player = null;
    private String music;
    
    public Sound(String music) {
        try {
            this.music = music;
            this.fls = new FileInputStream(this.music);
            this.player = new Player(fls);
            }
        catch (FileNotFoundException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }            catch (JavaLayerException ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
             
}
    @Override
    public void run(){
        try {
            this.player.play();
        } catch (JavaLayerException ex) {
            
        }
    }

}
