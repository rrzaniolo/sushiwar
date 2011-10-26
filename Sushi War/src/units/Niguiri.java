
package units;

/**
 * @author Hossomi
 * 
 * CLASS Niguiri ------------------------------------------
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import player.DirPad;
import player.Player;
import sprite.*;
import sushiwar.Constants;
import sushiwar.Screen;

public class Niguiri extends Unit implements Constants {

	private boolean			jumping		= false;
	private Player			player		= null;
	private NiguiriStatus	status		= null;
	private Crosshair		crosshair	= null;
	private String			name		= null;
	
	private static int		niguiriCount= 0;
	
	public enum NiguiriStatus {
		STAND, WALK, JUMP, FALL, LAND;
	}
	
	public Niguiri( double x, double y, Player player, Screen screen, boolean respondControl ) {
		super(x, y, 30, 30, screen, respondControl );
		setCollisionBox( 7, 9, 16, 16 );
		
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
		
		sprite.playAnimation("stand");
		
		//	--	Crosshair  --
		crosshair = new Crosshair( this, screen );
		
		//	--	Name  --
		name = "Niguiri " + player.getNumber() + ":" + niguiriCount;
		niguiriCount++;
				
		//	--	Stuff  --
		screen.frame.addKeyListener( this );
		this.player = player;
	}
	
	public void setStatus( NiguiriStatus now ) {
		if (status != now) {
			status = now;

			if (now == NiguiriStatus.WALK)
				playAnimation("walk");
			else if (now == NiguiriStatus.JUMP)
				playAnimation("jump");
			else if (now == NiguiriStatus.FALL)
				playAnimation("jump");
			else if (now == NiguiriStatus.LAND)
				playAnimation("land");
			else if (now == NiguiriStatus.STAND)
				playAnimation("stand");
		}
	}
	
	public void update() {
		super.update();
		
		if (!onAir && vy >= 0) {
			if (status == NiguiriStatus.JUMP)
				setStatus(NiguiriStatus.LAND);
			
			else if (status == NiguiriStatus.LAND)
				if (sprite.isDone())
					setStatus(NiguiriStatus.STAND);
		}
		
		//crosshair.setPosition( x, y );
	}
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		
		//	--	Pulo! \o/  --
		if ( flyHeight == 0 && e.getKeyCode() == MOVE_NIGUIRI_JUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_JUMP_VX, -MOVE_NIGUIRI_JUMP_VY );
			//jumping = true;
			setStatus(NiguiriStatus.JUMP);
			//playAnimation( "jump" );
		}
		
		else if ( flyHeight == 0 && e.getKeyCode() == MOVE_NIGUIRI_HJUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_HJUMP_VX, -MOVE_NIGUIRI_HJUMP_VY );
			//jumping = true;
			setStatus(NiguiriStatus.JUMP);
			//playAnimation( "jump" );
		}
		
		else if ( e.getKeyCode() == KeyEvent.VK_0)
			crosshair.changeAngle(5);
		
		else if ( e.getKeyCode() == KeyEvent.VK_9)
			crosshair.changeAngle(-5);
		
		if (!onAir && isMoving())
			setStatus(NiguiriStatus.WALK);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE )
			setPosition(screen.getRandomX(NIGUIRI_WIDTH),15);
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		if (!isMoving() && !jumping)
			this.setStatus(NiguiriStatus.STAND);
	}
	
	@Override
	public void print( Graphics g ){
		super.print(g);
		
		if (respondControl)
			crosshair.print(g);
		
		Graphics2D g2 = (Graphics2D) g;
		int size = g2.getFontMetrics().stringWidth(name);
		
		screen.setForeground( Player.getColor( player.getNumber() ) );
		g2.drawString( name, (float) x-size/2, (float) y-height/2-10 );
		
		//Graphics2D g2 = (Graphics2D) g;
		//g2.fill(collisionBox);//.fillRect( (int) collisionBox.getMinX(), (int) collisionBox.getMinY(), (int) collisionBox.width, (int) collisionBox.height );
	}
    
    public Player getPlayer(){
        return this.player;
    }
       
}
