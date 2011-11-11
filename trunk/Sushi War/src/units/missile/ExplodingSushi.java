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
	
	static final double		MAX_SPEED	= 100;
	
	public ExplodingSushi( Niguiri niguiri, double firePower, Screen screen ) {
		
		super( niguiri.getFireX(), niguiri.getFireY(), 30, 30, 20, 50, screen );
		
		sprite = new Sprite( "Sushi", 20, 20, screen, false );
		
		sprite.addAnimation( new Animation( "stand", 0, 8, 50, true ) );
		sprite.playAnimationByIndex(0);
		
		double angle = niguiri.getFireAngle();
		double speed = MAX_SPEED * firePower/100;
		applySpeed( speed*Math.cos(angle), speed*Math.sin(angle) );
		
		screen.addMissile(this);
		this.moveTimer.start();
	}
	
}
