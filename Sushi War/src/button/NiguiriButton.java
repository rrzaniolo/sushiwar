
package button;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

/**
 *
 * @author Hossomi
 */
public class NiguiriButton {
	
	private ButtonSprite		sprite;
	private String				text;
	private int					width;
	private int					x, y;
	private int					textOffset;
	private MouseStatus			mouseStatus;
	private JPanel				screen;
	private ButtonAction		action;
	private boolean				visible;
	
	private static final int	height = 30;
	
	public enum MouseStatus {
		OVER, NONE
	}
	
	public NiguiriButton( int x, int y, int width, String text, JPanel screen ) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.screen = screen;
		this.visible = true;
		
		sprite = new ButtonSprite( screen );
		sprite.playAnimation("close");
		
		screen.addMouseMotionListener( new MotionControl() );
		screen.addMouseListener( new MouseControl() );
		
	}
    
	public void setAction( ButtonAction action ) {
		this.action = action;
	}
	
	public void setVisible( boolean visible ) {
		this.visible = visible;
	}
	
	public void print( Graphics g ) {
		if (visible) {
			Graphics g2 = screen.getGraphics();
			FontMetrics fm = g2.getFontMetrics();

			textOffset = height - (height - fm.getHeight());

			g.setColor( Color.white );
			g.fillRect( x+23, y, width-23, height );
			g.setColor( Color.black );
			g.fillRect( x+24, y+1, width-25, height-2 );
			g.setColor( Color.white );
			g.drawString( text, x+50, y+textOffset);

			sprite.print( x, y+(height-45)/2 , g );
		}
	}
	
	private class MotionControl extends MouseMotionAdapter {
			
		public void update(MouseEvent e) {
			int mx = e.getX();
			int my = e.getY();
			if ( mx > x && mx < x+width && my > y && my < y+height ) {
				if (mouseStatus == MouseStatus.NONE)
					sprite.playAnimation("open");
				mouseStatus = MouseStatus.OVER;
			}
			else {
				if (mouseStatus == MouseStatus.OVER)
					sprite.playAnimation("close");
				mouseStatus = MouseStatus.NONE;
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			update(e);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			update(e);
		}
	}
	
	private class MouseControl extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (action != null && visible) {
				int mx = e.getX();
				int my = e.getY();
				if ( mx > x && mx < x+width && my > y && my < y+height )
					action.execute();
			}
		}
		
	}
	
}
