
package sushiwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class Screen extends JPanel implements Constants, MouseListener {
	
	public int width;
	public int height;
	private ArrayList<Niguiri> listNiguiri;
    private ArrayList<Player> listp;
	public JFrame frame;
	private Terrain terrain = null;
	
	public Screen( int w, int h, JFrame frame ) {
		super();
		
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
		for(int i=0; i<1; i++) {
			p = new Player(false, i, i, this);
			listp.add( p );
			p.createNiguiri();
			//frame.addKeyListener(p.n);
		}
		
		//	--	Restante!

		
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
	
	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		
		terrain.print(g);

                for (Player p: listp) {
                    p.printNiguiri(g);
                }
                
		//for( Niguiri n: list)
		//	n.print(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		terrain.explode( e.getX() - frame.getInsets().left, e.getY() - frame.getInsets().top, 30 );
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//throw new UnsupportedOperationException("Not supported yet.");
	}
	
}
