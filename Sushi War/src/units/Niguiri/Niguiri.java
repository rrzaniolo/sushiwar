
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
import sprite.*;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Unit;
import units.missile.ExplodingSushi;

public class Niguiri extends Unit implements Constants {

	//	--	Movimento  --
	private NiguiriStatus	status		= null;
	
	//	--	Configurações  --
	private Player			player		= null;
	private  Crosshair		crosshair	= null;
	private int				life		= 0;
	private String			name		= null;
	private InfoBar			infoBar		= null;
	
	private static int		niguiriCount= 0;
	
	public enum NiguiriStatus {
		STAND, WALK, JUMP, FALL, LAND;
	}
	
	//	-----------------------------------------------------------------------
	
	public Niguiri( double x, double y, Player player, Screen screen, boolean respondControl ) {
		super(x, y, 30, 30, screen, respondControl );
		setCollisionBox( 7, 9, 16, 16 );
		this.player = player;
		this.life = NIGUIRI_INITIAL_LIFE;
		
		//	--	Sprite  --
		sprite = new Sprite( "niguiri2", 30, 30, screen );
		
		//	--	Animations  --
		Animation anim;

		anim = new Animation("stand", 0, 6, 40, true);
		anim.setFramePeriod(0, 2500);
		sprite.addAnimation( anim );
		sprite.addAnimation( new Animation("walk", 6, 8, 40, true) );
		sprite.addAnimation( new Animation("jump", 14, 3, 30, false) );
		sprite.addAnimation( new Animation("land", 17, 7, 40, false) );
		
		sprite.playAnimation("Stand");
		status = NiguiriStatus.STAND;
		
		//	--	Crosshair  --
		crosshair = new Crosshair( this, screen );
		
		//	--	Name  --
		name = "Niguiri " + player.getId() + ":" + niguiriCount;
		niguiriCount++;
		
		infoBar = new InfoBar( this, screen );
				
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
		}
	}
	
	public void setLife( int life ) {
		this.life = life;
	}
	
	public void doDamage( int damage ) {
		life -= damage;
		if (life < 0)
			life = 0;
		
		infoBar.update();
	}
	
	public void toggle( boolean on ) {
		super.toggle(on);
		infoBar.setVisible(!on);
	}
	
	public int update() {
		int updateStatus = super.update();
		boolean hitGround = (updateStatus & Constants.MOVE_HITGROUND_VERTICAL) != 0;
			
		if (onAir && !hitGround)
			setStatus( NiguiriStatus.JUMP );
		
		if (hitGround && status == NiguiriStatus.JUMP) {
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
		
		/*if (onAir)
			setStatus(NiguiriStatus.FALL);
		
		if (!onAir) {
			if (status == NiguiriStatus.FALL && (hitGround|Constants.MOVE_HIT_GROUND) > 0)
				setStatus(NiguiriStatus.LAND);
			
			else if (status == NiguiriStatus.LAND || status == NiguiriStatus.FALL) {
				if (sprite.isDone())
					setStatus(NiguiriStatus.STAND);
			}
			
			else if (isMoving())
				setStatus(NiguiriStatus.WALK);
			else if (status == NiguiriStatus.WALK) {
				setStatus(NiguiriStatus.STAND);
			}
		}*/
		
		infoBar.update();
		crosshair.update();
		
		return 0;
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
		
		if (!onAir && isMoving())
			setStatus(NiguiriStatus.WALK);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE )
			setPosition(screen.getRandomX(NIGUIRI_WIDTH),15);
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			new ExplodingSushi( this, screen );
		}
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		if (!isMoving())
			this.setStatus(NiguiriStatus.STAND);
	}
	
	//	--  Gráfico  ----------------------------------------------------------
	
	@Override
	public void print( Graphics g ){
		super.print(g);
		
		if (respondControl && status == NiguiriStatus.STAND )
			crosshair.print(g);
		
		//Graphics2D g2 = (Graphics2D) g;
		//g2.fill(collisionBox);//.fillRect( (int) collisionBox.getMinX(), (int) collisionBox.getMinY(), (int) collisionBox.width, (int) collisionBox.height );
	}
       
}
