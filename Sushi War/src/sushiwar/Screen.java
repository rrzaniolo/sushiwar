
package sushiwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
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

public class Screen extends JPanel implements Constants {
	
	public Screen( int w, int h, JFrame frame ) {
		super();
		
		//	--	Inicializar janela
		setSize(w, h);
		setBackground(Color.decode("0xff8000"));
		
		this.setFont( new Font("Courier New", Font.PLAIN, 12));
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
		/*s1.addAnimation(a);
		s2.addAnimation(a2);
		s3.addAnimation(a3);
		s1.playAnimation("walk");
		s2.playAnimation("yes");
		s3.playAnimation("shit");*/
		
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
	
	private ArrayList<Niguiri> listNiguiri;
        private ArrayList<Player> listp;
	public JFrame frame;
	private Terrain terrain = null;

	
	/*private Sprite s1 = new Sprite("niguiri", 30, 30, this );
	private Sprite s2 = new Sprite("niguiri", 30, 30, this );
	private Sprite s3 = new Sprite("niguiri", 30, 30, this );*/
}