package units.Niguiri;

import units.Niguiri.Niguiri;
import java.awt.Graphics;
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
	
	
	public Crosshair( Niguiri niguiri, Screen screen ) {
		super(niguiri.getPositionX(), niguiri.getPositionY(), 40, 40, screen, false );
		
		this.niguiri = niguiri;
		sprite = new Sprite( "Crosshair", 50, 40, screen );
		
		Animation standAnim = new Animation("stand", 0, 8, 40, true );
		standAnim.setFramePeriod( 4, 200 );
		sprite.addAnimation( standAnim );
		
		sprite.playAnimation("stand");
	}
	
	public void update() {
		this.x = niguiri.getPositionX() + Math.cos(angle)*CROSSHAIR_RADIUS;
		this.y = niguiri.getPositionY() + Math.sin(angle)*CROSSHAIR_RADIUS;
				
		
	}
	
	public void print( Graphics g ) {
		if (sprite != null){
			sprite.print( x-width/2, y-height/2, g, angle );
		}
	}
	
	public void changeAngle( double delta ) {
		angle += delta*Math.PI/180;
	}
	
}
