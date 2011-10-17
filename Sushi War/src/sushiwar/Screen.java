
package sushiwar;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;
import timer.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import player.Player;
//import msgbox.MsgBox;
import scenario.Terrain;
import units.Agent;
import units.Niguiri;

/**
 * @author Hossomi
 * 
 * CLASS Screen -------------------------------------------
 * A parte gráfica da janela, onde tudo será mostrado.
 */

public class Screen extends JPanel implements Constants {
	
	public int width;
	public int height;
    private Player playerActive;
	private ArrayList<Niguiri> listNiguiri;
    private ArrayList<Player> listp;
    private Niguiri niguiriActive;
	public JFrame frame;
	private Terrain terrain = null;
	
	private int mouseX;
	private int mouseY;
	
	public Screen( int w, int h, JFrame frame ) {
		super();
		
		this.setLayout(null);
		this.addMouseListener( new MouseControl() );
		this.addMouseMotionListener( new MotionControl() );
        frame.addKeyListener (new KeyControl());
		
		//	--	Inicializar janela
		this.width = w;
		this.height = h;
		setSize(w, h);
		setBackground(SCREEN_DEFAULT_BGCOLOR);
		
		//this.setFont( new Font("Arial Round Bold", Font.BOLD, 12));
		this.setForeground(Color.white);
		
		//	--	Inicializar terreno
		terrain = new Terrain("land03", this);
		
		//	--	Inicializer niguiris teste
		Player p = null;

		this.frame = frame;

		listp = new ArrayList<Player>();
		for(int i=0; i<Constants.PLAYER_COUNT; i++) {
			p = new Player(false, i, i, this);
			listp.add( p );
			p.createNiguiri();
			//frame.addKeyListener(p.n);
		}
		
		//	--	Restante!
        playerActive = listp.get(0);
        niguiriActive = playerActive.getNiguiriList().get(playerActive.getNiguiriActive());
        niguiriActive.toggleControl(true);

		
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

		public void keyPressed(KeyEvent e){
			if(e.getKeyCode()== KeyEvent.VK_C){
				int atualPlayer = Screen.this.niguiriActive.getPlayer().getNumber();
				int nextPlayer = atualPlayer + 1;
				
				if (nextPlayer >= Constants.PLAYER_COUNT)
					nextPlayer = 0;
				
				Screen.this.playerActive = Screen.this.listp.get(atualPlayer);
				int atualNiguiri =  Screen.this.playerActive.getNiguiriActive();
				
				ArrayList<Niguiri> atualList = Screen.this.playerActive.getNiguiriList();
				atualList.get(atualNiguiri).toggleControl(false);

				Screen.this.playerActive = Screen.this.listp.get(nextPlayer);
				atualNiguiri =  Screen.this.playerActive.getNiguiriActive();
				atualList = Screen.this.playerActive.getNiguiriList();
				atualList.get(atualNiguiri).toggleControl(true);
				
				System.out.println(atualPlayer);
				Screen.this.niguiriActive = atualList.get(atualNiguiri);
				
				}
			}

	}

                      
            
            
        
	
	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		
		terrain.print(g);
		
		for (Player p: listp) {
			p.printNiguiri(g);
		}
		
        Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.drawString( "mouse x: " + mouseX, this.width - 300, 15);
		g2.drawString( "mouse y: " + mouseY, this.width - 300, 30);
		
		//for( Niguiri n: list)
		//	n.print(g);
	}
	
}
