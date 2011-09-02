
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
import player.DirPad;
import sprite.*;
import sushiwar.Screen;

public class Niguiri extends Unit {

	public enum NiguiriStatus {
		STAND, WALK, HAPPY, SAD;
	}
	
	public Niguiri( int x, int y, int player, Screen screen ) {
		super(x, y, 30, 30, screen );
		
		setCollisionBox( 7, 9, 16, 16 );
		sprite = new Sprite( "niguiri", 30, 30, screen );
		
		Animation anim;
		sprite.addAnimation( new Animation("walk", 0, 8, 40, true) );
		sprite.addAnimation( new Animation("yes", 8, 9, 40, false) );
		sprite.addAnimation( new Animation("shit", 17, 7, 60, true, 2) );
		anim = new Animation("stand", 24, 4, 40, true);
		anim.setFramePeriod(0, 2500);
		sprite.addAnimation( anim );
		sprite.playAnimation("stand");
		
		this.player = player;
	}
	
	@Override
	public void update() {
		super.update();
		
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
		System.out.println("Pressed");
		
		if (!falling && e.getKeyCode() == KeyEvent.VK_SPACE) {
			applySpeed( DirPad.Direction2X(facing), -3 );
			
			System.out.println(DirPad.Direction2X(facing));
		}
		
		if (this.isMoving())
			this.setStatus(NiguiriStatus.WALK);
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE )
			this.setPosition(screen.getWidth()/2,15);
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		System.out.println("Released");
		if (!this.isMoving())
			this.setStatus(NiguiriStatus.STAND);
	}
	
	@Override
	public void print( Graphics g ){
		super.print(g);
		
		//Graphics2D g2 = (Graphics2D) g;
		//g2.fill(collisionBox);//.fillRect( (int) collisionBox.getMinX(), (int) collisionBox.getMinY(), (int) collisionBox.width, (int) collisionBox.height );
	}
	
	private int player = 0;
	private NiguiriStatus status = null;
}
