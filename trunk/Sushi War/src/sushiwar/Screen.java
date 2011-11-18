
package sushiwar;

import button.ButtonAction;
import button.NiguiriButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import player.Player;
import scenario.Terrain;
import sound.Music;
import timer.Timer;
import timer.TimerListener;
import units.Agent;
import units.Niguiri.Niguiri;
import units.Niguiri.Niguiri.NiguiriStatus;
import units.missile.Missile;

/**
 * @author Hossomi
 * 
 * CLASS Screen -------------------------------------------
 * A parte gráfica da janela, onde tudo será mostrado.
 */

public class Screen extends JPanel implements Constants {
	
	public	int					width;
	public	int					height;
    private Player				playerActive	= null;
	private int					playerActiveId	= 0;
    private ArrayList<Player>	playerList		= null;
    private Niguiri				niguiriActive	= null;
	private	ArrayList<Missile>	missileList		= null;
	private ArrayList<Niguiri>	niguiriList		= null;
	private	JFrame				frame			= null;
	private	Terrain				terrain			= null;
	private GameStatus			gameStatus		= GameStatus.PLAYER_TURN;
	private Timer				gameTimer;
	private double				shakeMagnitude;
	private Timer				shakeTimer;
	
	private Image				background;
	
	private NiguiriButton		button;
	
	private int					mouseX;
	private int					mouseY;
	
	public enum GameStatus {
		PLAYER_TURN, MISSILE_FLY, EXPLOSION_TIME, DAMAGE_DEAL, NIGUIRI_DEATH
	}
	
	public Screen( int w, int h, JFrame frame, int numPlayers, int numNiguiris ) {
		super();
        
        InputStream is = Screen.class.getResourceAsStream( "/assets/InfoBarFont.ttf");
		try {
			Font theFont = Font.createFont( Font.TRUETYPE_FONT, is );
			setFont( theFont.deriveFont(Font.PLAIN, 20));
			
			
		} catch (FontFormatException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		Music gameMusic = new Music("Jinggle");
		
		//	--	Inicializar listas --
		missileList = new ArrayList<Missile>(0);
		niguiriList = new ArrayList<Niguiri>(0);
		
		//	--	Inicializar janela  --
		this.setLayout( null );
		this.addMouseListener( new MouseControl() );
		this.addMouseMotionListener( new MotionControl() );
        frame.addKeyListener (new KeyControl());
		
		this.frame = frame;
		this.width = w;
		this.height = h;
		setSize(w, h);
		setBackground(SCREEN_DEFAULT_BGCOLOR);
		
		URL url = Screen.class.getResource("/assets/background.png");
		background = new ImageIcon(url).getImage();
		this.setForeground(Color.white);
		
		//	--	Inicializar terreno  --
		terrain = new Terrain("land06", this);
		
		//	--	Inicializer jogadores  --

		playerList = new ArrayList<Player>();
		for(int i=0; i<numPlayers; i++) {
			playerList.add( new Player( numNiguiris, this ) );
		}
		
		playerActiveId = 0;
        playerActive = playerList.get(0);
        playerActive.toggle( true );

		for (Player p: playerList)
			p.startNiguiri();
		
		gameTimer = new Timer( new TimerControl(), 250 );
		gameTimer.start();
        
		gameMusic.play();
		
		shakeTimer = new Timer( new ShakeControl(), 100 );
		shakeTimer.start();
		
		//	--	Teste de botão  --
		button = new NiguiriButton( 20, 20, 150, "SAIR", (JPanel)this );
		button.setAction( new ActionControl() );
	}
	
	public void addNiguiri( Niguiri niguiri ) {
		niguiriList.add(niguiri);
		frame.addKeyListener(niguiri);
	}
	
	public void removeNiguiri( Niguiri niguiri ) {
		niguiriList.remove(niguiri);
		frame.removeKeyListener(niguiri);
	}
	
	public void addMissile( Missile missile ) {
		missileList.add(missile);
	}
	
	public void removeMissile( Missile missile ) {
		missileList.remove(missile);
	}
	
	public void removePlayer( Player player ) {
		playerList.remove(player);
	}
	
	public void setGameStatus( GameStatus status ) {
		gameStatus = status;
		System.out.println( status.toString() );
		if (status == GameStatus.PLAYER_TURN)
			pauseTurn(false);
		else
			pauseTurn(true);
	}
	
	public void setShakeMagnitude( double mag ) {
		shakeMagnitude = mag;
	}
	
	public void explode( double x, double y, int damage, double radius, double power ) {
		terrain.explode( x, y, radius );
		
		for (Niguiri target: niguiriList) {
			double dx = target.getPositionX() - x;
			double dy = target.getPositionY() - y;
			
			if (dx*dx + dy*dy <= radius*radius) {
				double angle = Math.atan2(dy, dx);
				
				target.applySpeed( power*Math.cos(angle), power*Math.sin(angle));
				target.applyDamage(damage);
				target.setStatus( NiguiriStatus.DIZZY );
			}
		}
		
		setGameStatus( GameStatus.EXPLOSION_TIME );
		setShakeMagnitude( power*10 );
	}
	
	public void pauseTurn( boolean pause ) {
		playerActive.toggle( !pause );
	}
	
	public void nextTurn() {
		playerActive.toggle(false);
				
		playerActiveId = ( playerActiveId + 1 ) % playerList.size();
		playerActive = playerList.get( playerActiveId );
		playerActive.nextNiguiri();
		playerActive.toggle( true );
		
		setGameStatus( GameStatus.PLAYER_TURN );
	}
	
	public int isPointInScreen( int x, int y ) {
		int result = 0;
		
		if (0 <= x && x <= this.getWidth())
			result += 1;
		
		if (0 <= y && y <= this.getHeight())
			result += 2;
		
		return result;
	}
	
	public int isBoxInScreen( Rectangle r ) {
		int result = 0;
		
		if (r.getMinX() < 0) {
			result += SCREEN_OUT_LEFT;
			if (r.getMaxX() < 0)
				result += SCREEN_OUT_TOTAL;
		}
		else if (r.getMaxX() > width) {
			result += SCREEN_OUT_RIGHT;
			if (r.getMinX() > width)
				result += SCREEN_OUT_TOTAL;
		}
		
		if (r.getMinY() < 0) {
			result += SCREEN_OUT_TOP;
			if (r.getMaxY() < 0)
				result += SCREEN_OUT_TOTAL;
		}
		else if (r.getMaxY() > height) {
			result += SCREEN_OUT_BOTTOM;
			if (r.getMinY() > height)
				result += SCREEN_OUT_TOTAL;
		}
			
		
		return result;
	}	
	
	public int adjustAgentInScreen( Agent ag ) {
		int result = this.isBoxInScreen( ag.getBox() );
		
		if ((result & SCREEN_OUT_TOP) != 0)
			ag.setPositionY( ag.getHeight()/2 );
		else if ((result & SCREEN_OUT_BOTTOM) != 0)
			ag.setPositionY( height - ag.getHeight()/2);
		
		if ((result & SCREEN_OUT_LEFT) != 0)
			ag.setPositionX( ag.getWidth()/2 );
		else if ((result & SCREEN_OUT_RIGHT) != 0)
			ag.setPositionX( width - ag.getWidth()/2);
		
		return result;		
	}
	
	public int getAgentFlyHeight( Agent ag ) {
		return terrain.getAgentFlyHeight(ag);
	}
	
	public boolean hitTerrain( Agent ag, boolean adjust ) {
		return terrain.collided(ag, adjust);
	}
	
	public int getRandomX( int size ) {
		return size/2 + (int) (Math.random() * (width-size));
	}
	
	public boolean checkMovement() {
		for (Niguiri n: niguiriList)
			if (n.getSpeed() > 0 )
				return true;
		
		return false;
	}
	
	public GameStatus getGameStatus() {
		return gameStatus;
	}
	
	public void update() {
		
		if ( gameStatus == GameStatus.PLAYER_TURN ) {
			
		}
		else if ( gameStatus == GameStatus.EXPLOSION_TIME ) {
			if (!checkMovement())
				setGameStatus( GameStatus.DAMAGE_DEAL );
		}
		else if ( gameStatus == GameStatus.DAMAGE_DEAL ) {
			for (Niguiri n: niguiriList )
				n.doDamage();
			
			try {
				Timer.sleep(2000);
			} catch (InterruptedException ex) {	}
			
			setGameStatus( GameStatus.NIGUIRI_DEATH );
		}
		else if ( gameStatus == GameStatus.NIGUIRI_DEATH ) {
			for (Niguiri n: niguiriList )
				if (n.getLife() == 0)
					n.kill();
			try {
				Timer.sleep(2000);
			} catch (InterruptedException ex) { }
			
			nextTurn();
		}
	}
	
	public void updateShake() {
		shakeMagnitude /= 2;
		if (shakeMagnitude < 3)
			shakeMagnitude = 0;
	}
	
	//	--	Classes de controle de eventos  --
	
	class MouseControl extends MouseAdapter {
		
        @Override
		public void mousePressed( MouseEvent e ) {
			terrain.explode( e.getX(), e.getY(), 30 );
		}
		
	}
	
	class MotionControl extends MouseMotionAdapter {
		
        @Override
		public void mouseMoved( MouseEvent e ) {
			Screen.this.mouseX = e.getX();
			Screen.this.mouseY = e.getY();
		}
		
	}
        
	class KeyControl extends KeyAdapter {

        @Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()== KeyEvent.VK_C){
				
			}
		}

	}

	class TimerControl implements TimerListener {

		@Override
		public int update() {
			Screen.this.update();
			return 0;
		}
		
	}
	
	class ShakeControl implements TimerListener {

		@Override
		public int update() {
			Screen.this.updateShake();
			return 0;
		}
		
	}
	
	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		g.drawImage( background, 0, 0, null );
		
		double sx = Math.random()*shakeMagnitude - shakeMagnitude/2;
		double sy = Math.random()*shakeMagnitude - shakeMagnitude/2;

		terrain.print(g, sx, sy);
		
		for (Player p: playerList) {
			p.printNiguiri(g, sx, sy);
		}
		
		for (Niguiri n: niguiriList) {
			n.printCrosshair(g, sx, sy);
		}
		
        for (Missile m: missileList)
			m.print(g);
		
		//	--	Teste de botão  --
		button.print(g);
		
		//for( Niguiri n: list)
		//	n.print(g);
	}

	//	--	TESTE DE BOTÃAAAAAAAO --
	
	private class ActionControl extends ButtonAction {

		@Override
		public void execute() {
			System.exit(0);
		}
		
	}
	
}
