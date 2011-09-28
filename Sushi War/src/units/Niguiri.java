
package units;

/**
 * @author Hossomi
 * 
 * CLASS Niguiri ------------------------------------------
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import player.DirPad;
import player.Player;
import sprite.*;
import sushiwar.Constants;
import sushiwar.Screen;

public class Niguiri extends Unit implements Constants {

	public enum NiguiriStatus {
		STAND, WALK, HAPPY, SAD;
	}
	
	public Niguiri( double x, double y, Player player, Screen screen ) {
		super(x, y, 30, 30, screen );
		
		setCollisionBox( 7, 9, 16, 16 );
		sprite = new Sprite( "niguiri2", 30, 30, screen );
		
		/*Animation anim;
		sprite.addAnimation( new Animation("walk", 6, 8, 30, true) );
		sprite.addAnimation( new Animation("yes", 8, 9, 30, false) );
		sprite.addAnimation( new Animation("shit", 17, 7, 40, true, 2) );
		anim = new Animation("stand", 0, 6, 40, true);
		anim.setFramePeriod(0, 2500);
		sprite.addAnimation( anim );*/
		
		Animation anim;
		sprite.addAnimation( new Animation("walk", 6, 8, 40, true) );
		sprite.addAnimation( new Animation("yes", 6, 8, 30, false) );
		sprite.addAnimation( new Animation("shit", 17, 7, 40, true, 2) );
		anim = new Animation("stand", 0, 6, 40, true);
		anim.setFramePeriod(0, 2500);
		sprite.addAnimation( anim );
		
		sprite.playAnimation("stand");
		
		screen.frame.addKeyListener( this );
		this.player = player;
	}
	
	public boolean playAnimation( String anim ) {
		return sprite.playAnimation(anim);
	}
	
	public void setStatus( NiguiriStatus now ) {
		if (status != now) {
			status = now;

			if (now == NiguiriStatus.WALK)
				playAnimation("walk");
			else if (now == NiguiriStatus.HAPPY)
				playAnimation("yes");
			else if (now == NiguiriStatus.SAD)
				playAnimation("shit");
			else if (now == NiguiriStatus.STAND)
				playAnimation("stand");
		}
	}
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);
		
		//	--	Pulo! \o/  --
		if ( flyHeight == 0 && e.getKeyCode() == MOVE_NIGUIRI_JUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_JUMP_VX, -MOVE_NIGUIRI_JUMP_VY );
		}
		
		else if ( flyHeight == 0 && e.getKeyCode() == MOVE_NIGUIRI_HJUMP_KEY ) {
			setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_HJUMP_VX, -MOVE_NIGUIRI_HJUMP_VY );
		}
		
		if (this.isMoving())
			this.setStatus(NiguiriStatus.WALK);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE )
			this.setPosition(screen.getRandomX(NIGUIRI_WIDTH),15);
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		if (!this.isMoving())
			this.setStatus(NiguiriStatus.STAND);
	}
	
	@Override
	public void print( Graphics g ){
		super.print(g);
		
		//Graphics2D g2 = (Graphics2D) g;
		//g2.fill(collisionBox);//.fillRect( (int) collisionBox.getMinX(), (int) collisionBox.getMinY(), (int) collisionBox.width, (int) collisionBox.height );
	}
	
	private Player player = null;
	private NiguiriStatus status = null;
}
