/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;
/*Testing*/
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;
import sushiwar.Screen;
import units.Niguiri;
import java.awt.event.KeyListener;

/**
 *
 * @author Daron Vardmir
 */
public class Player {
    public Player (boolean turn, int number, int niguiri, Screen scr){
    this.turn = turn;
    this.number = number;
    this.scr = scr;
    this.list = new ArrayList<Niguiri>();
		for( int i=0; i<5; i++) {
			this.n = new Niguiri(15+(i%30)*30,15+(i/30)*30,1,scr);
			this.list.add( this.n );
			this.scr.frame.addKeyListener(this.n);
                }
    
}
    
    protected boolean turn = false;
    protected int number = 0;
    protected ArrayList<Niguiri> list;
    protected Screen scr;
    protected Niguiri n;
    protected JFrame frame;
    
    public void printNiguiri( Graphics g ) {
        for (Niguiri n: list)
            n.print(g);
    }
}     