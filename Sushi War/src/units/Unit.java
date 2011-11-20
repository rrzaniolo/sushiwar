
package units;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	
	//	--	Imagem  --
	protected Sprite sprite = null;
	
	//	--	Movimento  --
	protected boolean	respondGravity	= true;
	protected boolean	respondWind		= false;
	protected boolean	ready			= true;
	protected boolean	onAir			= false;
	protected boolean	blockMovement	= false;
	protected double	flyHeight		= 0;
	protected double	vx				= 0;
	protected double	vy				= 0;
	protected Direction facing			= null;
	
	//	--	Debug  --
	private static final boolean showDebug = false;
	
	//	-----------------------------------------------------------------------
	
	public Unit( double x, double y, int width, int height, Screen screen ) {
		super(x, y, width, height, screen, true);
		
		facing = Direction.RIGHT;
	}
	
	public Unit( double x, double y, int width, int height, Screen screen, boolean respondControl ) {
		super(x, y, width, height, screen, respondControl);
	}	
	
	public Unit( double x, double y, int width, int height, Screen screen, boolean respondControl, boolean respondGravity, boolean respondWind ) {
		super(x, y, width, height, screen, respondControl);
		this.respondGravity = respondGravity;
		this.respondWind = respondWind;
	}	
	
	//	--	Manipulação  ------------------------------------------------------
	
	/**
	 * Move a unidade de acordo com gravidade, vento e controles.
	 */
	@Override
	public int update() {
		
		int returnStatus = 0;
		
		//	--  Gravidade  --
		
		if (respondGravity)
			vy += GRAVITY/MOVE_TIMER_PERIOD;
		
		//	--	Mover em Y  --
		//	Será movido apenas se estiver acima do solo.
		//	Cancelar movimento espontâneo em x caso atinja o solo
		
		//	Calcular altura em relação ao terreno
		flyHeight = screen.getAgentFlyHeight(this);
		
		//	Calcular deslocamento sem atravessar o terreno
		double dy = Math.min( flyHeight, vy/MOVE_TIMER_PERIOD );

		if (!blockMovement && dy != 0)
			this.move(0, dy);
				//vy = 0;
		
		//	Se o deslocamento tiver sido menor que a velocidade, significa que
		//	atingiu o terreno
		if (dy < vy/MOVE_TIMER_PERIOD) {
			vx = 0;
			vy = 0;
			returnStatus += Constants.MOVE_HITGROUND_VERTICAL;
		}
		
		//	Se a altura atual for maior que a altura de queda, unidade está
		//	caindo
		this.onAir = (flyHeight > MOVE_FALLING_HEIGHT);
		
		//	--  Mover em X  --
		//	Aplicar movimento de controle!
		//	Retroceder movimento caso seja impossível
		
		double dx = vx;
		
		//	Se não estiver caindo e responder a controles, aplicar a velocidade
		//	de controle
		if (ready && respondControl)
			dx += ux * MOVE_NIGUIRI_SPEED;
		
		dx = dx/MOVE_TIMER_PERIOD;
		if (!blockMovement)
			this.move( dx, 0 );
		
		//	Se tiver atingido terreno, cancelar movimento em X.
		if (screen.hitTerrain(this, vy >= -MOVE_FALLING_HEIGHT)) {
			this.move(-dx,0);
			vx = 0;
			vy = Math.max(vy, 0);
			returnStatus += Constants.MOVE_HITGROUND_HORIZONTAL;
		}
		
		return returnStatus;
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
	
	public boolean playAnimation( String anim ) {
		return sprite.playAnimation(anim);
	}
	
	//	--	Informação  -------------------------------------------------------
	
	public boolean isFacing( Direction dir ) {
		if (facing == dir)
			return true;
		
		return false;
	}
	
	public double getSpeed() {
		return Math.sqrt( vx*vx + vy*vy );
	}
	
	//	--	Eventos  ----------------------------------------------------------
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		
		if (!blockMovement) {
			if (ux == -1)
				facing = Direction.LEFT;
			else if (ux == 1)
				facing = Direction.RIGHT;
		}
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
	
	public void print( Graphics g, double sx, double sy ) {
		if (sprite != null){
			double nx = x-width/2 + sx;
			double ny = y-height/2 + sy;
			
			sprite.print( nx, ny, g, facing);
		}
	}

}
