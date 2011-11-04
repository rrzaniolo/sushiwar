/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units.missile;

import sprite.Animation;
import sprite.Sprite;
import sushiwar.Screen;
import units.Niguiri.Niguiri;

/**
 *
 * @author Hossomi
 */
public class ExplodingSushi extends Missile {
	
	static final double		speed	= 50;
	
	public ExplodingSushi( Niguiri niguiri, Screen screen ) {
		
		super( (int) niguiri.getPositionX(), (int) niguiri.getPositionY(), 30, 30, 20, 50, screen );
		
		sprite = new Sprite( "Sushi", 20, 20, screen );
		sprite.addAnimation( new Animation( "stand", 0, 3, 20 ) );
		
		double angle = niguiri.crosshair.angle;
		applySpeed( speed*Math.cos(angle), speed*Math.sin(angle) );
		
		screen.missileList.add(this);
		this.moveTimer.start();
	}
	
}
