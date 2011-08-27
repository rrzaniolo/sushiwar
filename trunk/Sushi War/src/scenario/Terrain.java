/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scenario;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import sprite.Sprite;
import sushiwar.Screen;
import units.Agent;

/**
 *
 * @author Hossomi
 */
public class Terrain {
	
	public Terrain( String file, Screen screen ) {
		//	Carregando spritesheet
		URL fileURL = Sprite.class.getResource("/assets/" + file + ".png");
		Image tmpImage = new ImageIcon(fileURL).getImage();
		
		//	Copiando para uma BufferedImage, para que
		//	possamos cortá-la em sprites
		landImage = new BufferedImage( tmpImage.getWidth(null), tmpImage.getHeight(null), BufferedImage.TYPE_INT_ARGB );
		Graphics g = landImage.getGraphics();
		g.drawImage(tmpImage, 0, 0, null);
		g.dispose();
		
		//	Restante
		this.screen = screen;
	}
	
	//	--  Informação  -------------------------------------------------------
	
	public boolean collided( Agent ag ) {
		Rectangle box = ag.getBox();
		int x, y;
		int dx, dy;
		int cx = (int) box.getCenterX();
		int cy = (int) box.getCenterY();
		
		for (y = (int) box.getMinY(); y < box.getMaxY(); y++) {
			for (x = (int) box.getMinX(); x < box.getMaxX(); x++) {
				dx = x - cx;
				dy = y - cy;
				if (((landImage.getRGB(x, y) & 0x11000000) != 0) && (Math.sqrt(dx*dx+dy*dy) < ag.getRadius())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void print( Graphics g ) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage( landImage, 0, 0, null );
	}
	
	private Screen screen = null;
	private BufferedImage landImage = null;
}
