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
import sushiwar.Constants;
import sushiwar.Screen;
import units.Agent;

/**
 *
 * @author Hossomi
 */
public class Terrain implements Constants{
	
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
	
	/**
	 * Verifica se um agente colidiu com o terreno. Se adjust for verdadeiro,
	 * também vai ajustar para que o agente fique sobre o pixel mais alto
	 * se estiver dentro de uma tolerância.
	 * 
	 * @param ag O agente a verificar
	 * @param adjust Se vedadeiro, posiciona o agente sobre o pixel mais alto
	 * se estiver dentro da tolerãncia
	 * @return Verdadeiro se colidir
	 */
	
	public boolean collided( Agent ag, boolean adjust ) {
		Rectangle box = ag.getCollisionBox();
		int x, y;
		int minX, minY, maxX, maxY;
		int maxCliff = 0;
		
		//	--	Encontrar a caixa onde verificar
		minX = (int) Math.max( box.getMinX(), 0 );
		minY = (int) Math.max( box.getMinY(), 0 );
		maxX = (int) Math.min( box.getMaxX(), landImage.getWidth() );
		maxY = (int) Math.min( box.getMaxY(), landImage.getHeight() );
		
		//	--	Verificar pixels
		for (y = (int) minY; y < maxY; y++) {
			for (x = (int) minX; x < maxX; x++) {
				if (((landImage.getRGB(x, y) & 0x11000000) != 0)) {
					
					//	--	Calcular pixel mais alto
					maxCliff = Math.min(y - maxY, maxCliff);
					
					//	--	Se não for preciso ajustar, retornar caso encontre
					//	--	um pixel
					if (!adjust)
						return true;
					
				}
			}
		}
		
		//	--	Se estiver dentro da tolerância, mover
		if (maxCliff > -MOVE_CLIFF_TOLERANCE) {
			ag.move(0, maxCliff);
			return false;
		}
			
		return true;
		
	}
	
	/**
	 * Calcula a altura de um agente em relação ao terreno atual.
	 * A altura será em relação ao pixel mais próximo da base do agente, 
	 * considerando toda sua extensão.
	 * @param ag Agente a ser avaliado
	 * @return A altura em relação ao pixel mais próximo do agente. Caso já
	 * esteja em contato com o terreno, retorna 0.
	 */
	
	public int getAgentFlyHeight( Agent ag ) {
		Rectangle box = ag.getCollisionBox();
		int x, y;
		int minX, maxX, minY, maxY;
		
		minX = (int) Math.max( box.getMinX(), 0 );
		minY = (int) Math.max( box.getMaxY(), 0 );
		maxX = (int) Math.min( box.getMaxX(), screen.getWidth() );
		maxY = (int) screen.height;
		
		for (y = minY; y < maxY; y++)
			for (x = minX; x < maxX; x++)
				if (((landImage.getRGB(x, y) & 0x11000000) != 0)) {
					return y - minY;
				}
		
		return maxY - minY;
	}
	
	/**
	 * Cria um buraco circular no terreno.
	 * 
	 * @param x Coordenada x do centro da explosão
	 * @param y Coordenada y do centro da explosão
	 * @param r Raio da explosão
	 */
	public void explode( double cx, double cy, double r ) {
		int minX = (int) Math.max( cx-r, 0 );
		int minY = (int) Math.max( cy-r, 0 );
		int maxX = (int) Math.min( cx+r, screen.width  );
		int maxY = (int) Math.min( cy+r, screen.height  );
		
		r = r*r;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				
				double dx = x - cx;
				double dy = y - cy;
				
				if (dx*dx + dy*dy <= r)
					landImage.setRGB( x, y, 0x00000000);
			}
		}
	}
	
	public void print( Graphics g ) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage( landImage, 0, 0, null );
	}
	
	private Screen screen = null;
	private BufferedImage landImage = null;
}
