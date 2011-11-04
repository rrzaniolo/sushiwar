
package sushiwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import player.Player;
import scenario.Terrain;
import units.Agent;
import units.Niguiri.Niguiri;
import units.missile.Missile;

/**
 * @author Hossomi
 * 
 * CLASS Screen -------------------------------------------
 * A parte gráfica da janela, onde tudo será mostrado.
 */

public class Screen extends JPanel implements Constants {
	
	public	int					width;
	public	int					height;
    private Player				playerActive	= null;
	private int					playerActiveId	= 0;
    private ArrayList<Player>	playerList		= null;
    private Niguiri				niguiriActive	= null;
	public	ArrayList<Missile>	missileList		= null;
	public	JFrame				frame			= null;
	public	Terrain				terrain			= null;
	
	private int					mouseX;
	private int					mouseY;
	
	public Screen( int w, int h, JFrame frame ) {
		super();
		
		//	--	Inicializar janela  --
		this.setLayout( null );
		this.addMouseListener( new MouseControl() );
		this.addMouseMotionListener( new MotionControl() );
        frame.addKeyListener (new KeyControl());
		
		this.frame = frame;
		this.width = w;
		this.height = h;
		setSize(w, h);
		setBackground(SCREEN_DEFAULT_BGCOLOR);
		
		//this.setFont( new Font("Arial Round Bold", Font.BOLD, 12));
		this.setForeground(Color.white);
		
		//	--	Inicializar terreno  --
		terrain = new Terrain("land03", this);
		
		//	--	Inicializer jogadores  --

		playerList = new ArrayList<Player>();
		for(int i=0; i<Constants.PLAYER_COUNT; i++) {
			playerList.add( new Player( PLAYER_NIGUIRI_COUNT, this ) );
		}
		
		playerActiveId = 0;
        playerActive = playerList.get(0);
        playerActive.toggle( true );

		for (Player p: playerList)
			p.startNiguiri();
		
		missileList = new ArrayList<Missile>(0);
	}
	
	public int isPointInScreen( int x, int y ) {
		int result = 0;
		
		if (0 <= x && x <= this.getWidth())
			result += 1;
		
		if (0 <= y && y <= this.getHeight())
			result += 2;
		
		return result;
	}
	
	public int isBoxInScreen( Rectangle r ) {
		int result = 0;
		
		if (r.getMinX() < 0) {
			result += SCREEN_OUT_LEFT;
			if (r.getMaxX() < 0)
				result += SCREEN_OUT_TOTAL;
		}
		else if (r.getMaxX() > width) {
			result += SCREEN_OUT_RIGHT;
			if (r.getMinX() > width)
				result += SCREEN_OUT_TOTAL;
		}
		
		if (r.getMinY() < 0) {
			result += SCREEN_OUT_TOP;
			if (r.getMaxY() < 0)
				result += SCREEN_OUT_TOTAL;
		}
		else if (r.getMaxY() > height) {
			result += SCREEN_OUT_BOTTOM;
			if (r.getMinY() > height)
				result += SCREEN_OUT_TOTAL;
		}
			
		
		return result;
	}	
	
	public int adjustAgentInScreen( Agent ag ) {
		int result = this.isBoxInScreen( ag.getBox() );
		
		if ((result & SCREEN_OUT_TOP) != 0)
			ag.setPositionY( ag.getHeight()/2 );
		else if ((result & SCREEN_OUT_BOTTOM) != 0)
			ag.setPositionY( height - ag.getHeight()/2);
		
		if ((result & SCREEN_OUT_LEFT) != 0)
			ag.setPositionX( ag.getWidth()/2 );
		else if ((result & SCREEN_OUT_RIGHT) != 0)
			ag.setPositionX( width - ag.getWidth()/2);
		
		return result;		
	}
	
	public int getAgentFlyHeight( Agent ag ) {
		return terrain.getAgentFlyHeight(ag);
	}
	
	public boolean hitTerrain( Agent ag, boolean adjust ) {
		return terrain.collided(ag, adjust);
	}
	
	public int getRandomX( int size ) {
		return size/2 + (int) (Math.random() * (width-size));
	}
	
	//	--	Classes de controle de eventos  --
	
	class MouseControl extends MouseAdapter {
		
		public void mousePressed( MouseEvent e ) {
			terrain.explode( e.getX(), e.getY(), 30 );
		}
		
	}
	
	class MotionControl extends MouseMotionAdapter {
		
		public void mouseMoved( MouseEvent e ) {
			Screen.this.mouseX = e.getX();
			Screen.this.mouseY = e.getY();
		}
		
	}
        
	class KeyControl extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()== KeyEvent.VK_C){
				playerActive.toggle(false);
				
				playerActiveId = ( playerActiveId + 1 ) % PLAYER_COUNT;
				playerActive = playerList.get( playerActiveId );
				playerActive.nextNiguiri();
				playerActive.toggle( true );
			}
		}

	}

	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		
		terrain.print(g);
		
		for (Player p: playerList) {
			p.printNiguiri(g);
		}
		
        for (Missile m: missileList)
			m.print(g);
		//for( Niguiri n: list)
		//	n.print(g);
	}
	
}
