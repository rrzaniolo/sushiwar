package units.Niguiri;

import java.awt.Graphics;
import javax.swing.JPanel;
import player.DirPad.Direction;
import sprite.Animation;
import sprite.Sprite;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Agent;

/**
 *
 * @author Hossomi
 */
public class Crosshair extends Agent implements Constants {
	
	private Niguiri niguiri = null;
	private Sprite sprite = null;
	private double angle = 0;
	private double rotation = 0;
	
	
	public Crosshair( Niguiri niguiri, Screen screen ) {
		super(niguiri.getPositionX(), niguiri.getPositionY(), 40, 40, screen, false );
		
		this.niguiri = niguiri;
		sprite = new Sprite( "Crosshair", 50, 40, (JPanel)screen );
		
		Animation standAnim = new Animation("stand", 0, 8, 40, true );
		standAnim.setFramePeriod( 4, 200 );
		sprite.addAnimation( standAnim );
		
		sprite.playAnimation("stand");
	}
	
	public int update() {		
		if (niguiri.isFacing( Direction.RIGHT ))
			angle = rotation;
		else
			angle = Math.PI - rotation;
		
		this.x = niguiri.getPositionX() + Math.cos(angle)*CROSSHAIR_RADIUS;
		this.y = niguiri.getPositionY() + Math.sin(angle)*CROSSHAIR_RADIUS;
				
		return 0;
	}
	
	public void print( Graphics g ) {
		if (sprite != null){
			sprite.print( x-width/2, y-height/2, g, angle );
		}
	}
	
	public void print( Graphics g, double sx, double sy ) {
		if (sprite != null){
			sprite.print( x-width/2 + sx, y-height/2 + sy, g, angle );
		}
	}
	
	public void changeAngle( double delta ) {
		rotation -= delta*Math.PI/180;
		rotation = Math.min(rotation, CROSSHAIR_ANGLE_MAX);
		rotation = Math.max(rotation, CROSSHAIR_ANGLE_MIN);
	}

	double getAngle() {
		return angle;
	}
	
}
