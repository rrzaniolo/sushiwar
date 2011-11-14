
package sushiwar;

// -- Manipula a execução dos son e musicas do Jogo --

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
        music = music.replaceAll("%20", " ");
        try {
            this.music = music;
            this.fls = new FileInputStream(this.music);
            this.player = new Player(fls);
            }
        catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            }catch (JavaLayerException ex) {
                System.out.println(ex.getMessage());
            }
             
}
    @Override
    public void run(){
        try {
            this.player.play();
        } catch (JavaLayerException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
