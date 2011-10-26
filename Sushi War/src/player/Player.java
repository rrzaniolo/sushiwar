
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;
/*Testing*/
import java.awt.Color;
import java.awt.Graphics;
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
	
	protected boolean turn = false;
    protected int number = 0;
    protected ArrayList<Niguiri> list;
    protected Screen screen;
    protected int niguiriActive;
    protected JFrame frame;
	
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
		
        int i;
		for (i = 0; i < PLAYER_NIGUIRI_COUNT; i++) {
			Niguiri n = new Niguiri( screen.getRandomX(NIGUIRI_WIDTH), 15, this, screen, false );
			list.add(n);
		}
       
        this.niguiriActive = 0;
        
	}
    
    public int getNumber(){
            return this.number;
    }
    
    public ArrayList<Niguiri> getNiguiriList(){
        return this.list;
    }
    
    public int getNiguiriActive(){
        return this.niguiriActive;
    }
    
    public void printNiguiri( Graphics g ) {
        for (Niguiri n: list)
            n.print(g);
    }
	
	public static Color getColor( int i ) {
		if (i==0)
			return Color.red;
		else if (i==1)
			return Color.green;
		
		return Color.white;
	}
}
