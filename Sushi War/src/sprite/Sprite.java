
package sprite;

/* @author Hossomi
 * 
 * CLASS Sprite -------------------------------------------
 * Controla a animação de uma imagem.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import player.DirPad.Direction;
import sushiwar.Constants;
import sushiwar.Screen;
import timer.*;

public class Sprite implements TimerListener, Constants {

	private BufferedImage spriteSheet;
	private BufferedImage []spritesRight;
	private BufferedImage []spritesLeft;
	private ArrayList<Animation> animations;
	private Dimension size;
	private Timer timer;
	
	private Animation animNow;
	private int frameNow;
	private int timeCount;
	private Screen screen;
	
	public Sprite( String file, int width, int height, Screen screen ) {	 
		
		//	Carregando spritesheet
		URL fileURL = Sprite.class.getResource("/assets/" + file + ".png");
		Image tmpImage = new ImageIcon(fileURL).getImage();
		
		//	Copiando para uma BufferedImage, para que
		//	possamos cortá-la em sprites
		spriteSheet = new BufferedImage( tmpImage.getWidth(null), tmpImage.getHeight(null), BufferedImage.TYPE_INT_ARGB );
		Graphics g = spriteSheet.getGraphics();
		g.drawImage(tmpImage, 0, 0, null);
		g.dispose();
		
		//	Cortar em sprites
		int x = 0;
		int y = 0;
		spritesRight = new BufferedImage[100];
		spritesLeft = new BufferedImage[100];
		
		for (int i=0; i<100; i++) {
			x = (i/50)*(width*5+1) + (i%5)*width;
			y = (i/5)%10*height;
			
			//	Sprite normal (direita)
			spritesRight[i] = spriteSheet.getSubimage(x, y, width, height);

			//	Sprite espelhado (esquerda)
			spritesLeft[i] = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			
			Graphics2D g2 = (Graphics2D) spritesLeft[i].getGraphics();
			
			AffineTransform aft = new AffineTransform();
			aft.translate(width, 0);
			aft.scale(-1.0, 1.0);
			
			g2.drawImage(spritesRight[i], aft, null);
			g2.dispose();
		}
		
		//	Inicializando restante 
		animations = new ArrayList<Animation>(0);
		timer = new Timer( this, SPRITE_TIMER_PERIOD );
		
		this.screen = screen;
	}
	
	//	--	Manipulação	 --
	
	/**
	 * Adiciona uma animação ao sprite.
	 * @param add Animação a ser adicionada.
	 */
	public void addAnimation( Animation add ) {
		animations.add( add );
		if (animations.size() == 1) {
			animNow = add;
		}
	}
	
	/**
	 * Inicia uma animação do sprite baseada em
	 * seu ID. O ID de uma animação é a ordem em que
	 * ela foi adicionada ao sprite.
	 * 
	 * @param id ID da animação a ser iniciada.
	 * @return Verdadeiro se a animação existir. Falso caso
	 * contrário.
	 */
	public boolean playAnimationByIndex( int id ) {
		if (id >= animations.size())
			return false;
		
		animNow = animations.get(id);
		timeCount = animNow.getFramePeriod(0);
		frameNow = 0;
		
		if (!timer.isAlive())
			timer.start();
		
		return true;
	}
	
	public boolean playAnimation( String name ) {
	 int id = 0;
	 Animation tmp = null;
	 
		do {
			tmp = animations.get(id);
			id++;
		} while ( !name.equals(tmp.getName()) && id < animations.size() );
		
		if (id-1 == animations.size())
			return false;
		
		animNow = tmp;
		timeCount = animNow.getFramePeriod(0);
		frameNow = 0;
		
		if (!timer.isAlive())
			timer.start();
		
		return true;
	}

	@Override
	public void update() {
		if (animNow != null) {
			timeCount -= SPRITE_TIMER_PERIOD;
		
			if (timeCount <= 0) {
				frameNow = animNow.getNextFrame( frameNow  );
				timeCount = animNow.getFramePeriod( frameNow );
			}
		}	
		
		screen.repaint();
		
	}
	
	public boolean isDone() {
		return !animNow.isLooping() && frameNow == animNow.getFrameCount()-1;
	}
	
	//	--	Gráfico  --
	public void print( double x, double y, Graphics g ) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage( spritesRight[frameNow + animNow.getStartFrame()], (int) x, (int) y, null );
	}
        
	public void print( double x, double y, Graphics g, Direction facing){
		Graphics2D g2 = (Graphics2D) g;           
		if (facing == Direction.LEFT)
			g2.drawImage ( spritesLeft[frameNow + animNow.getStartFrame()], (int) x, (int) y, null);
		else
			g2.drawImage( spritesRight[frameNow + animNow.getStartFrame()], (int) x, (int) y, null);
	   
	}

}
