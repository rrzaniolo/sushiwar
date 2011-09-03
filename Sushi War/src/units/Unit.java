
package units;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import player.DirPad.Direction;
import sprite.Sprite;
import sushiwar.Constants;
import sushiwar.Screen;

/**
 * @author Hossomi
 * 
 * CLASS Unit ---------------------------------------------
 * Representação gráfica de um agente, contendo um sprite
 * animado.
 */

public class Unit extends Agent implements Constants {
	
	public Unit( int x, int y, int width, int height, Screen screen ) {
		super(x, y, width, height, screen, true);
		
		facing = Direction.RIGHT;
	}
	
	public Unit( int x, int y, int width, int height, Screen screen, boolean respondControl ) {
		super(x, y, width, height, screen, respondControl);
	}	
	
	public Unit( int x, int y, int width, int height, Screen screen, boolean respondControl, boolean respondGravity, boolean respondWind ) {
		super(x, y, width, height, screen, respondControl);
		this.respondGravity = respondGravity;
		this.respondWind = respondWind;
	}	
	
	//	--	Manipulação  ------------------------------------------------------
	
	/**
	 * Move a unidade se responder a controles.
	 * Move a unidade se responder à gravidade.
	 * Move a unidade se responder ao vento.
	 */
	@Override
	public void update() {
		
		//	--  Gravidade
		if (respondGravity)
			vy += gravity;
		
		//	--  Vento
		//	To do!
		
		//	--  Mover em X
		double dx;
		
		if (!falling && respondControl)
			dx = (vx + ux*moveSpeed);
		else
			dx = vx;
		
		this.move( dx, 0 );
		if (screen.hitTerrain(this, true)) {
			this.move(-dx,0);
			vx = 0;
			vy = Math.max(vy, 0);
		}
		
		//	--	Mover em Y
		int flyHeight = screen.getAgentFlyHeight(this);

		vy = Math.min( flyHeight, vy );
		
		if (vy != 0)
			this.move(0, vy);
		else
			vx = 0;
		
		this.falling = (flyHeight > 5);
		
	}
	
	/**
	 * Incrementa a velocidade automática.
	 * @param vx Incremento em x
	 * @param vy Incremento emm y
	 */
	public void applySpeed( double vx, double vy ) {
		this.vx += vx;
		this.vy += vy;
	}
	
	/**
	 * Redefine a velocidade automática.
	 * @param vx Velocidade em x
	 * @param vy Velocidade em y
	 */
	public void setSpeed( double vx, double vy ) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public void setMoveSpeed( double moveSpeed ) {
		this.moveSpeed = moveSpeed;
	}
	
	//	--	Eventos  ----------------------------------------------------------
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		
		if (ux == -1)
			facing = Direction.LEFT;
		else if (ux == 1)
			facing = Direction.RIGHT;
	}
	  
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
	}
	
	/**
	 * Imprime o sprite da unidade centrado em
	 * sua posição atual.
	 * @param g Elemento onde imprimir.
	 */
	public void print( Graphics g ) {
		if (sprite != null){
			sprite.print( x-width/2, y-height/2, g, facing);
		}
	}
	
	//	-----------------------------------------------------------------------
	
	protected Sprite sprite = null;
	protected boolean respondGravity = true;
	protected boolean respondWind = false;
	protected boolean falling = false;
	
	protected double vx = 0;
	protected double vy = 0;
	protected double moveSpeed = 0.5;
	
	protected Direction facing = null;
	
	public static double gravity = 0.1;
}
