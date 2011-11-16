
package units.Niguiri;

/**
 * @author Hossomi
 * 
 * CLASS Niguiri ------------------------------------------
 */

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import player.DirPad;
import player.Player;
import sound.Music;
import sound.Sound;
import sprite.*;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Unit;
import units.missile.ExplodingSushi;
import units.missile.PowerBar;

public class Niguiri extends Unit implements Constants {

	//	--	Movimento  --
	private NiguiriStatus	status		= null;
	
	//	--	Configurações  --
	private Player			player		= null;
	private Crosshair		crosshair	= null;
	private int				life		= 0;
	private int				damageTaken = 0;
	private String			name		= null;
	private InfoBar			infoBar		= null;
	private PowerBar		powerBar	= null;
	
	private static int		niguiriCount= 0;
	
	public enum NiguiriStatus {
		STAND, WALK, JUMP, FALL, LAND, DIZZY, FIRE, CRY, DIE;
	}
	
	//	-----------------------------------------------------------------------
	
	public Niguiri( double x, double y, Player player, Screen screen, boolean respondControl ) {
		super(x, y, 30, 30, screen, respondControl );
		setCollisionBox( 7, 9, 16, 16 );
		this.player = player;
		this.life = NIGUIRI_INITIAL_LIFE;
		
		sprite = new NiguiriSprite( screen );
		status = NiguiriStatus.STAND;
		
		//	--	Crosshair  --
		crosshair = new Crosshair( this, screen );
		
		//	--	Name  --
		name = "Niguiri " + player.getId() + ":" + niguiriCount;
		niguiriCount++;
		
		infoBar = new InfoBar( this, screen );
				
		//	--	Power bar  --
		powerBar = new PowerBar( this, screen );
		
		//	--	Stuff  --
		screen.addNiguiri( this );
	}
	
	//	--  Manipulação  ------------------------------------------------------
	
	public void setStatus( NiguiriStatus now ) {
		if (status != now) {
			status = now;

			if (now == NiguiriStatus.WALK) {
				playAnimation("walk");
				ready = true;
			}
			else if (now == NiguiriStatus.JUMP) {
				playAnimation("jump");
				ready = false;
			}
			else if (now == NiguiriStatus.FALL) {
				ready = false;
			}
			else if (now == NiguiriStatus.LAND) {
				playAnimation("land");
				ready = false;
			}
			else if (now == NiguiriStatus.STAND) {
				playAnimation("stand");
				ready = true;
			}
			else if (now == NiguiriStatus.DIZZY) {
				playAnimation("dizzy");
				ready = false;
			}
			else if (now == NiguiriStatus.FIRE) {
				playAnimation("fire");
				ready = false;
			}
			else if (now == NiguiriStatus.CRY) {
				playAnimation("cry");
				ready = false;
			}
			else if (now == NiguiriStatus.DIE) {
				playAnimation("die");
				ready = false;
			}
		}
	}
	
	public void setLife( int life ) {
		this.life = life;
	}
	
	public void applyDamage( int damage ) {
		damageTaken += damage;
	}
	
	public void doDamage() {
		life = Math.max( life -= damageTaken, 0 );
		damageTaken = 0;
	}
	
	public void kill() {
		setStatus( NiguiriStatus.DIE );
	}
	
	public void fire( int power ) {
		ExplodingSushi sushi = new ExplodingSushi( this, power, screen );
		powerBar.toggle(false);
		setStatus( NiguiriStatus.STAND );
		
	}
	
	public void toggle( boolean on ) {
		super.toggle(on);
		infoBar.setVisible(!on);
	}
	
	public int update() {
		int updateStatus = super.update();
		boolean hitGround = (updateStatus & Constants.MOVE_HITGROUND_VERTICAL) != 0;
		
		if (status == NiguiriStatus.DIE) {
			if (sprite.isDone()) {
				remove();
			}
		}
		
		else if (onAir && !hitGround) {
			if (damageTaken == 0)
				setStatus( NiguiriStatus.JUMP );
			else
				setStatus( NiguiriStatus.DIZZY );
		}
		
		if (hitGround && (status == NiguiriStatus.JUMP || status == NiguiriStatus.DIZZY) ) {
			setStatus( NiguiriStatus.LAND );
		}
		
		if ( status == NiguiriStatus.LAND ) {
			if (sprite.isDone()) {
				if (isMoving())
					setStatus( NiguiriStatus.WALK );
				else
					setStatus( NiguiriStatus.STAND );
			}
		}
				
		infoBar.update();
		crosshair.update();
		
		return 0;
	}
	
	public void remove() {
		player.removeNiguiri(this);
		moveTimer.finish();
		sprite.remove();
		screen.removeNiguiri(this);
	}
	
	//	--  Informação  -------------------------------------------------------
	
	public Player getPlayer(){
        return this.player;
    }
	
	public int getPlayerId() {
		return player.getId();
	}
	
	public String getName() {
		return name;
	}
	
	public int getLife() {
		return life;
	}
	
	public double getFireX() {
		return x + Constants.NIGUIRI_FIRE_RADIUS * Math.cos( crosshair.getAngle() );
	}
	
	public double getFireY() {
		return y + Constants.NIGUIRI_FIRE_RADIUS * Math.sin( crosshair.getAngle() );
	}
	
	public double getFireAngle() {
		return crosshair.getAngle();
	}
	
	public int getDamageTaken() {
		return damageTaken;
	}
	
	public String toString() {
		return name;
	}
	
	//	--  Eventos  ----------------------------------------------------------
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		
		//	--	Pulo normal  --
		if ( ready && e.getKeyCode() == MOVE_NIGUIRI_JUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_JUMP_VX, -MOVE_NIGUIRI_JUMP_VY );
			setStatus(NiguiriStatus.JUMP);
		}
		
		//	--	Pulo alto  --
		else if ( ready && e.getKeyCode() == MOVE_NIGUIRI_HJUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_HJUMP_VX, -MOVE_NIGUIRI_HJUMP_VY );
			setStatus(NiguiriStatus.JUMP);
		}
		
		else if ( e.getKeyCode() == KeyEvent.VK_UP)
			crosshair.changeAngle(5);
		
		else if ( e.getKeyCode() == KeyEvent.VK_DOWN)
			crosshair.changeAngle(-5);
		
		else if ( ready && e.getKeyCode() == KeyEvent.VK_SPACE) {
			setStatus( NiguiriStatus.FIRE );
			powerBar.reset();
			powerBar.toggle(true);
		}
		
		if (!onAir && isMoving())
			setStatus(NiguiriStatus.WALK);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE )
			setPosition(screen.getRandomX(NIGUIRI_WIDTH),15);
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		
		if ( status == NiguiriStatus.FIRE && e.getKeyCode() == KeyEvent.VK_SPACE ) {
			fire( powerBar.getPercentage() );
		}
		
		if (!isMoving())
			this.setStatus(NiguiriStatus.STAND);
	}
	
	//	--  Gráfico  ----------------------------------------------------------
	
	@Override
	public void print( Graphics g ){
		super.print(g);
	}
	
	public void printCrosshair( Graphics g ) {
		if (respondControl && status == NiguiriStatus.STAND )
			crosshair.print(g);
	}
       
}
