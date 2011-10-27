
package units.Niguiri;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import player.Player;
import sushiwar.Constants;
import sushiwar.Screen;
import units.Agent;

/**
 *
 * @author Hossomi
 */
public class InfoBar implements Constants {
	
	private JLabel		nameLabel			= null;
	private JLabel		lifeLabel			= null;
	private Niguiri		niguiri				= null;
	
	static final Color	BACKGROUND_COLOR	= Color.black;
	static final Color	BORDER_COLOR		= Color.white;
	
	static final int	NAMELABEL_WIDTH		= 100;
	static final int	LABEL_HEIGHT		= 18;
	static final int	LIFELABEL_WIDTH		= 30;
	
	
	public InfoBar( Niguiri niguiri, Screen screen ) {
		this.niguiri = niguiri;
		
		//	--	Rótulo de nome  --
		nameLabel = new JLabel( niguiri.getName() );
		nameLabel.setOpaque(true);
		nameLabel.setBackground( BACKGROUND_COLOR );
		nameLabel.setBorder( new LineBorder( BORDER_COLOR ) );
		nameLabel.setForeground( Player.getColor( niguiri.getPlayerId() ) );
		
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
		
		lifeLabel.setBounds( (int) niguiri.getPositionX() - LIFELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*2),
							 LIFELABEL_WIDTH, LABEL_HEIGHT );
		
		lifeLabel.setHorizontalAlignment( JLabel.CENTER );
		screen.add( lifeLabel );
		
	}
	
	void update() {
		
		nameLabel.setBounds( (int) niguiri.getPositionX() - NAMELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*3+1),
							 NAMELABEL_WIDTH, LABEL_HEIGHT );
		lifeLabel.setText( String.valueOf( niguiri.getLife() ) );
		lifeLabel.setBounds( (int) niguiri.getPositionX() - LIFELABEL_WIDTH/2,
							 (int) (niguiri.getPositionY() - niguiri.getHeight()/2 - LABEL_HEIGHT*2),
							 LIFELABEL_WIDTH, LABEL_HEIGHT );
	}
	
	void setVisible( boolean is ) {
		nameLabel.setVisible(is);
		lifeLabel.setVisible(is);
	}
	
}
