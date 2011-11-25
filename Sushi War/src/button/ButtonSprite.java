/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package button;

import javax.swing.JPanel;
import sprite.Animation;
import sprite.Sprite;

/**
 *
 * @author Hossomi
 */
public class ButtonSprite extends Sprite {
	
	public ButtonSprite( JPanel screen ) {
		super( "ButtonNiguiri", 45, 45, screen );
		
		addAnimation( new Animation( "open", 3, 4, 20 ));
		addAnimation( new Animation( "close", 0, 4, 20 ));
	}
	
}
