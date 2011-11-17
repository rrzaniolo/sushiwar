
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
import units.Niguiri.Niguiri;

/**
 *
 * @author Daron Vardmir
 */
public final class Player implements Constants {
	
    private ArrayList<Niguiri>	niguiriList		= null;
    private int					niguiriActiveId	= 0;
	private Niguiri				niguiriActive	= null;
	private int					niguiriCount	= 0;
	private Screen				screen			= null;
	private int					id				= 0;
	private boolean				active			= false;
	
	private static int			count			= 0;
	private static final Color[]color			= { Color.red, Color.yellow, Color.green, Color.cyan, Color.orange, Color.magenta };
	
    public Player ( int niguiriCount, Screen screen){
		this.id = Player.count;
		this.screen = screen;
		this.niguiriCount = niguiriCount;
		this.niguiriList = new ArrayList<Niguiri>( niguiriCount );
		
		this.createNiguiri();
		
		Player.count++;
		
	}
	
	public void createNiguiri() {
		for (int i = 0; i < niguiriCount; i++)
			niguiriList.add( new Niguiri( screen.getRandomX(NIGUIRI_WIDTH), 15, this, screen, false ) );
       
        this.niguiriActiveId = 0;
        this.niguiriActive = niguiriList.get(0);
	}
    
	public void printNiguiri( Graphics g ) {
        for (Niguiri n: niguiriList)
            n.print(g);
    }
	
	public void toggle( boolean on ) {
		this.active = on;
		this.niguiriActive.toggle(on);
	}
	
	public void toggleActiveNiguiri( boolean on ) {
		this.niguiriActive.toggle(on);
	}
	
	public void nextNiguiri() {
		niguiriActiveId = (niguiriActiveId + 1) % niguiriList.size();
		niguiriActive = niguiriList.get( niguiriActiveId );
	}
	
	public void removeNiguiri( Niguiri niguiri ) {
		if (niguiriList.size() == 1)
			screen.removePlayer(this);
		else if (niguiri == niguiriActive)
				nextNiguiri();
		
		niguiriList.remove(niguiri);
	}
	
	public void startNiguiri() {
		for (Niguiri n: niguiriList)
			n.startTimer();
	}
	
    public Niguiri getNiguiriActive(){
        return this.niguiriList.get( niguiriActiveId );
    }
	
	public int getId(){
            return id;
    }
	
	public static Color getColor( int i ) {
		if ( i >= 0 && i < color.length )
			return color[i];
		
		return Color.white;
	}
}
