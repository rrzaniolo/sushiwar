package msgbox;

import java.awt.Color;
import java.awt.Graphics;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Agent;

/**
 *
 * @author Hossomi
 */

public class MsgBox extends Agent implements Constants {

	private String msg = null;
	
	public MsgBox( String msg, int x, int y, Screen screen) {
		super(x, y, 30, 30, screen, true);
		
		this.msg = msg;
		
	}
	
	public void print( Graphics g ) {
		g.drawString( msg, (int) x,  (int) y);
	}
	
}
