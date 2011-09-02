
package units;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import player.DirPad.Direction;
import sprite.Sprite;
import sushiwar.Screen;

/**
 * @author Hossomi
 * 
 * CLASS Unit ---------------------------------------------
 * Representação gráfica de um agente, contendo um sprite
 * animado.
 */

public class Unit extends Agent {
	
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
	
		int result, flyHeight;
		
		//	--  Mover em X
		
		result = this.move( ux*speed + vx, 0 );
		if (screen.hitTerrain(this, true)) {
			this.move(-(ux*speed + vx),0);
			vx = 0;
		}
		
		flyHeight = screen.getAgentFlyHeight(this);

		vy = Math.min( flyHeight, vy );
		
		if ( vy == 0 ) {
			falling = false;
			vx = 0;
		}
		else {
			falling = true;
			move(0, vy);
		}
		
//		if (!screen.hitTerrain(this, false)) {
//			result = this.move( 0, vy );
//
//			//	Parar de cair caso saia para baixo da tela
//			if ((result & Screen.SCREEN_OUT_BOTTOM) != 0 || screen.hitTerrain(this, false)) {
//				this.move(0,-vy);
//				vy = 0;
//				vx = ux*speed;
//				falling = false;
//			}
//			else {
//				falling = true;
//			}
//		}
			
		
	}
	
	public void applySpeed( double vx, double vy ) {
		this.vx += vx;
		this.vy += vy;
	}
	
	public void setSpeed( double vx, double vy ) {
		this.vx = vx;
		this.vy = vy;
	}
	
	//	--	Eventos  ----------------------------------------------------------
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		if (!falling)
			vx = ux * speed;
		
		if (ux == -1)
			facing = Direction.LEFT;
		else if (ux == 1)
			facing = Direction.RIGHT;
	}
	  
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		if (!falling)
			vx = ux * speed;
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
	
	protected Sprite sprite = null;
	protected boolean respondGravity = true;
	protected boolean respondWind = false;
	
	protected double vx = 0;
	protected double vy = 0;
	protected Direction facing = null;
	protected boolean falling = false;
	
	public static double gravity = 0.1;
	
	private static double speed = 0.85;
}
