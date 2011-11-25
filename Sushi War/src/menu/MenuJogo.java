
package menu;

import button.ButtonAction;
import button.NiguiriButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import scenario.Terrain;
import sprite.Animation;
import sprite.Sprite;
import sushiwar.Constants;
import sushiwar.Main;
import sushiwar.Main.MenuStatus;

public class MenuJogo extends JPanel implements Constants {
	private static Image		terrainBackground;
	private int					currentTerrain;
	private TerrainPreview[]	terrainPreview;
	
	private int					currentPlayerCount;
	private PlayerPreview		playerPreview;
	private Main				frame;
	
	private NiguiriButton		back;
	private NiguiriButton		play;
	
	private int					width;
	private int					height;
	private Image				background;
	
	private final static int	PLAYER_PREVIEW_HEIGHT = 465;
	
	public MenuJogo( int w, int h, Main frame ) {
		super();

		//	--	Inicializar janela  --
		this.frame = frame;
		this.width = w;
		this.height = h;
		this.setLayout(null);

		setSize(w, h);
		setBackground(Color.black);
		URL url = MenuPrincipal.class.getResource("/assets/gameMenuBackground.png");
		background = new ImageIcon(url).getImage();
		
		//	--	Inicializando fonte  --
        InputStream is = MenuPrincipal.class.getResourceAsStream( "/assets/InfoBarFont.ttf");
		try {
			Font theFont = Font.createFont( Font.TRUETYPE_FONT, is );
			setFont( theFont.deriveFont(Font.PLAIN, 20));			
		} catch (FontFormatException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		//	--	Inicializar previsão de terreno  --
		//	Background
		AffineTransform transf = AffineTransform.getScaleInstance(0.5, 0.5);
		url = MenuJogo.class.getResource("/assets/background.png");
		Image img = new ImageIcon(url).getImage();
		
		terrainBackground = new BufferedImage( img.getWidth(null)/2, img.getHeight(null)/2, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2 = (Graphics2D) terrainBackground.getGraphics();
		g2.drawImage(img, transf, null);
		
		//	Previews
		terrainPreview = new TerrainPreview[ MENU_TERRAIN_NAMES.length ];
		for (int i = 0; i < terrainPreview.length; i++)
			terrainPreview[i] = new TerrainPreview( MENU_TERRAIN_NAMES[i], width/4, height/8 );
		
		addMouseListener( terrainPreview[0] );
		addMouseMotionListener( terrainPreview[0] );
		
		//	--	Inicializar número de jogadores  --
		
		currentPlayerCount = 2;
		playerPreview = new PlayerPreview( PLAYER_MIN, PLAYER_MAX, currentPlayerCount );
		addMouseListener( playerPreview );
		addMouseMotionListener( playerPreview );
		
		//	--	Inicializar botões  --
		
		back = new NiguiriButton( 50,(height - 60) , 200, "VOLTAR", this );
        back.setAction( new BackActionControl() );
		
		play = new NiguiriButton( (width - 250),(height - 60) , 200, "JOGAR", this );
        play.setAction( new PlayActionControl() );
		
	}
	
	public void hideMenu() {
		setVisible(false);
	}
	
	public void showMenu() {
		setVisible(true);
	}
	
	public void paintComponent( Graphics g ) {
		super.paintComponent(g);
		g.drawImage( background, 0, 0, null);
		
		terrainPreview[ currentTerrain ].print(g);
		playerPreview.print(g);
		
		back.print(g);
		play.print(g);
	}
	
	private class TerrainPreview extends MouseAdapter implements MouseMotionListener {
		private Image preview;
		private boolean over;
		private int x, y;
		
		public TerrainPreview( String name, int x, int y ) {
			this.x = x;
			this.y = y;
			
			AffineTransform transf = AffineTransform.getScaleInstance(.5, .5);
			URL url = MenuJogo.class.getResource("/assets/" + name + ".png");
			
			Image img = new ImageIcon( url ).getImage();
			BufferedImage bimg = new BufferedImage( img.getWidth(null)/2, img.getHeight(null)/2, BufferedImage.TYPE_INT_ARGB );
			
			Graphics2D g2 = (Graphics2D) bimg.getGraphics();
			g2.drawImage( img, transf, null );
			
			for (int j = 0; j < img.getHeight(null)/2; j++)
				for (int i = 0; i < img.getWidth(null)/2; i++)
					if ( (bimg.getRGB(i, j) & 0x11000000) != 0)
						bimg.setRGB(i, j, 0xffffc080);
			
			preview = new BufferedImage( img.getWidth(null)/2, img.getHeight(null)/2, BufferedImage.TYPE_INT_ARGB );
			Graphics g = preview.getGraphics();
			g.drawImage( terrainBackground, 0, 0, null );
			g.drawImage( bimg, 0, 0, null );
		}
		
		public void print(Graphics g ) {
			g.setColor( Color.white );
			g.fillRect(x-5, y-5, preview.getWidth(null)+10, preview.getHeight(null)+10);
			
			if (over) {
				g.setColor( Color.black );
				g.drawRect(x-4, y-4, preview.getWidth(null)+7, preview.getHeight(null)+7);
			}
			
			g.drawImage( preview, x, y, null );
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getX() > x && e.getX() < x + preview.getWidth(null)
				&& e.getY() > y && e.getY() < y + preview.getHeight(null) ) {
				
				removeMouseListener( terrainPreview[ currentTerrain ] );
				removeMouseMotionListener( terrainPreview[ currentTerrain ] );
				currentTerrain = (currentTerrain + 1) % terrainPreview.length;
				addMouseListener( terrainPreview[ currentTerrain ] );
				addMouseMotionListener( terrainPreview[ currentTerrain ] );
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (e.getX() > x && e.getX() < x + preview.getWidth(null)
				&& e.getY() > y && e.getY() < y + preview.getHeight(null) )
				
				over = true;
			
			else
				over = false;
			
			repaint();
		}
		
	}
	
	private class PlayerPreview extends MouseAdapter implements MouseMotionListener {
		private int x, y, height, width;
		private Sprite playerSprite;
		private boolean over;
		private int min, max;

		public PlayerPreview( int min, int max, int current ) {
			playerSprite = new Sprite( "ButtonNiguiri", 45, 45, MenuJogo.this );
			playerSprite.addAnimation( new Animation( "open", 3, 4, 20 ));
			playerSprite.addAnimation( new Animation( "close", 0, 4, 20 ));
			
			this.max = max;
			this.min = min;
			this.x = MenuJogo.this.width/2 - (playerSprite.getWidth()/2) * currentPlayerCount;
			this.y = PLAYER_PREVIEW_HEIGHT;
			this.width = playerSprite.getWidth()*currentPlayerCount;
			this.height = playerSprite.getHeight();
		}
		
		public void print( Graphics g ) {
			for (int i = 0; i < currentPlayerCount; i++)
				playerSprite.print( x + i*playerSprite.getWidth(), y, g);
		}
		
		public void addPlayer( int delta ) {
			currentPlayerCount += delta;
			if (currentPlayerCount < min)
				currentPlayerCount = min;
			else if (currentPlayerCount > max)
				currentPlayerCount = max;
			
			x = MenuJogo.this.width/2 - (playerSprite.getWidth() * currentPlayerCount)/2;
			width = playerSprite.getWidth()*currentPlayerCount;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if ( e.getX() > x && e.getX() < x + width
				&& e.getY() > y && e.getY() < y + height ) {
				
				if (e.getButton() == MouseEvent.BUTTON1)
					addPlayer(1);
				else
					addPlayer(-1);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if ( e.getX() > x && e.getX() < x + width
				&& e.getY() > y && e.getY() < y + height ) {
				
				if (!over)
					playerSprite.playAnimation("open");
				over = true;
			}
			
			else {
				if (over)
					playerSprite.playAnimation("close");
				over = false;
			}
		}		
		
	}
	
	private class BackActionControl extends ButtonAction {

		@Override
		public void execute() {
			frame.toggleMenu( MenuStatus.MENU_MAIN );
		}
		
	}
	
	private class PlayActionControl extends ButtonAction {

		@Override
		public void execute() {
			frame.startGame( currentPlayerCount, MENU_TERRAIN_NAMES[ currentTerrain ] );
		}
		
	}
	
}
