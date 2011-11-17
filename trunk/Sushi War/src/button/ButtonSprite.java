/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package button;

import sprite.Animation;
import sprite.Sprite;
import sushiwar.Screen;

/**
 *
 * @author Hossomi
 */
public class ButtonSprite extends Sprite {
	
	public ButtonSprite( Screen screen ) {
		super( "ButtonNiguiri", 45, 45, screen );
		
		addAnimation( new Animation( "open", 0, 4, 20 ));
		addAnimation( new Animation( "close", 0, 1, 1 ));
	}
	
}
