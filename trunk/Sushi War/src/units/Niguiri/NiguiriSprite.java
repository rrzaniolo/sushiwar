
package units.Niguiri;

import sound.Sound;
import sprite.Animation;
import sprite.Sprite;
import sushiwar.Screen;

/**
 *
 * @author Hossomi
 */
public class NiguiriSprite extends Sprite {
	
	public NiguiriSprite( Screen screen ) {
		//	--	Sprite  --
		super( "niguiri4", 30, 30, screen );
		
		//	--	Animations  --
		Animation anim;
		
		anim = new Animation("stand", 0, 6, 40, true);
		anim.setFramePeriod(0, 2500);
		this.addAnimation( anim );
		this.addAnimation( new Animation("walk", 6, 8, 40, true) );
		this.addAnimation( new Animation("jump", 14, 3, 30, false) );
		this.addAnimation( new Animation("land", 17, 7, 40, false) );
		this.addAnimation( new Animation("dizzy", 24, 8, 25, true) );
		this.addAnimation( new Animation("fire", 32, 3, 40, false) );
		anim = new Animation("cry", 35, 8, 60, true);
		anim.setFramePeriod(4, 1000);
		this.addAnimation( anim );
		anim = new Animation("die", 43, 12, 60, false);
		anim.setFramePeriod(3, 1000);
		this.addAnimation( anim );
		
		this.playAnimation("Stand");
	}
	
	public int update() {
		super.update();
		
		if (frameNow == 0) {
			if (animNow.getName().equals("walk")) {
				Sound walkSound = new Sound("NiguiriMove");
				walkSound.play();
			}
		}
		
		return 0;
	}
	
}
