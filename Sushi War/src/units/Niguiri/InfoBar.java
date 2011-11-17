
package units.Niguiri;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import player.Player;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Unit;

/**
 *
 * @author Hossomi
 */
public class InfoBar implements Constants {
	
	private JLabel		nameLabel			= null;
	private JLabel		lifeLabel			= null;
	private Niguiri		niguiri				= null;
	private Screen		screen;
	
	static final Color	BACKGROUND_COLOR	= Color.black;
	static final Color	BORDER_COLOR		= Color.white;
	
	static final int	NAMELABEL_WIDTH		= 100;
	static final int	LIFELABEL_WIDTH		= 30;
	static final int	LABEL_HEIGHT		= 20;
	
	static private Font	LABEL_FONT			= null;
	
	
	public InfoBar( Niguiri niguiri, Screen screen ) {
		
		//	--	Inicializar fonte  --
		if (LABEL_FONT == null) {
			InputStream url = Unit.class.getResourceAsStream( "/assets/InfoBarFont.ttf");
			try {
				LABEL_FONT = Font.createFont( Font.TRUETYPE_FONT , url );
				LABEL_FONT = LABEL_FONT.deriveFont( Font.PLAIN, 13);
			}
			catch (FontFormatException ex) {}
			catch (IOException ex) {}
			
		}
		
		//	--	Inicializar barra  --
		this.niguiri = niguiri;
		
		//	--	Rótulo de nome  --
		nameLabel = new JLabel( niguiri.getName() );
		nameLabel.setOpaque(true);
		nameLabel.setBackground( BACKGROUND_COLOR );
		nameLabel.setBorder( new LineBorder( BORDER_COLOR ) );
		nameLabel.setForeground( Player.getColor( niguiri.getPlayerId() ) );
		nameLabel.setFont( LABEL_FONT );
		
		nameLabel.setBounds( (int) niguiri.getPositionX() - NAMELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*3),
							 NAMELABEL_WIDTH, LABEL_HEIGHT );
		
		nameLabel.setHorizontalAlignment( JLabel.CENTER );
		screen.add( nameLabel );
		
		//	--	Rótulo de vida  --
		lifeLabel = new JLabel( String.valueOf(niguiri.getLife()) );
		lifeLabel.setOpaque(true);
		lifeLabel.setBackground( BACKGROUND_COLOR );
		lifeLabel.setBorder( new LineBorder( BORDER_COLOR ) );
		lifeLabel.setForeground( Player.getColor( niguiri.getPlayerId() ) );
		lifeLabel.setFont( LABEL_FONT );
		
		lifeLabel.setBounds( (int) niguiri.getPositionX() - LIFELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*2),
							 LIFELABEL_WIDTH, LABEL_HEIGHT );
		
		lifeLabel.setHorizontalAlignment( JLabel.CENTER );
		screen.add( lifeLabel );
		
		this.screen = screen;
		
	}
	
	public void update() {
		
		nameLabel.setLocation( (int) niguiri.getPositionX() - NAMELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*3+1) );
		lifeLabel.setText( String.valueOf( niguiri.getLife() ) );
		lifeLabel.setLocation( (int) niguiri.getPositionX() - LIFELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*2) );
	}
	
	public void remove() {
		screen.remove(nameLabel);
		screen.remove(lifeLabel);
	}
	
	public void setVisible( boolean is ) {
		nameLabel.setVisible(is);
		lifeLabel.setVisible(is);
	}
	
}
