
package sushiwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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

public class Screen extends JPanel implements Constants, KeyListener {
	
	public Screen( int w, int h, JFrame frame ) {
		super();
		
		//	--	Inicializar janela
		setSize(w, h);
		setBackground(Color.decode("0xff8000"));
		
		this.setFont( new Font("Courier New", Font.BOLD, 12));
		this.setForeground(Color.red);
		
		//	--	Inicializar terreno
		terrain = new Terrain("land03", this);
		
		//	--	Inicializer niguiris teste
		Niguiri n = null;
                Player p = null;

                this.frame = frame;
                
                listp = new ArrayList<Player>();
                for(int i=0; i<2; i++) {
                    p = new Player(false, i, i, this);
                    listp.add( p );
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
		else if (r.getMaxX() > this.getWidth()) {
			result += SCREEN_OUT_RIGHT;
			if (r.getMinX() > this.getWidth())
				result += SCREEN_OUT_TOTAL;
		}
		
		if (r.getMinY() < 0) {
			result += SCREEN_OUT_TOP;
			if (r.getMaxY() < 0)
				result += SCREEN_OUT_TOTAL;
		}
		else if (r.getMaxY() > this.getHeight()) {
			result += SCREEN_OUT_BOTTOM;
			if (r.getMinY() > this.getHeight())
				result += SCREEN_OUT_TOTAL;
		}
			
		
		return result;
	}	
	
	public int adjustAgentInScreen( Agent ag ) {
		int result = this.isBoxInScreen( ag.getBox() );
		
		if ((result & SCREEN_OUT_TOP) != 0)
			ag.setPositionY( ag.getHeight()/2 );
		else if ((result & SCREEN_OUT_BOTTOM) != 0)
			ag.setPositionY( this.getHeight() - ag.getHeight()/2);
		
		if ((result & SCREEN_OUT_LEFT) != 0)
			ag.setPositionX( ag.getWidth()/2 );
		else if ((result & SCREEN_OUT_RIGHT) != 0)
			ag.setPositionX( this.getWidth() - ag.getWidth()/2);
		
		return result;		
	}
	
	public int getAgentFlyHeight( Agent ag ) {
		return terrain.getAgentFlyHeight(ag);
	}
	
	public boolean hitTerrain( Agent ag, boolean adjust ) {
		return terrain.collided(ag, adjust);
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
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_E)
			terrain.explode( listNiguiri.get(0).getPositionX(), listNiguiri.get(0).getPositionY(), 50 );
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	private ArrayList<Niguiri> listNiguiri;
        private ArrayList<Player> listp;
	public JFrame frame;
	private Terrain terrain = null;
}
