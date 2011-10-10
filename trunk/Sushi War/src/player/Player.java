
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;
/*Testing*/
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Niguiri;

/**
 *
 * @author Daron Vardmir
 */
public class Player implements Constants {
	
    public Player (boolean turn, int number, int niguiri, Screen scr){
		this.turn = turn;
		this.number = number;
		this.screen = scr;
		this.list = new ArrayList<Niguiri>();

		/*for( int i=0; i<5; i++) {
			double r = Math.random();
			System.out.println((int)(r*scr.getWidth()));
			this.n = new Niguiri( (int) (r * scr.getWidth()), 0, this, scr);
			this.list.add( this.n );
			this.screen.frame.addKeyListener(this.n);
		}*/
		
	}
	
	public void createNiguiri() {
		
		for (int i = 0; i < PLAYER_NIGUIRI_COUNT-1; i++) {
			Niguiri n = new Niguiri( screen.getRandomX(NIGUIRI_WIDTH), 15, this, screen, false );
			list.add(n);
		}
                Niguiri n = new Niguiri( screen.getRandomX(NIGUIRI_WIDTH), 15, this, screen, true );
		list.add(n);
	}
               
    protected boolean turn = false;
    protected int number = 0;
    protected ArrayList<Niguiri> list;
    protected Screen screen;
    protected Niguiri n;
    protected JFrame frame;
    
    public void printNiguiri( Graphics g ) {
        for (Niguiri n: list)
            n.print(g);
    }
}
