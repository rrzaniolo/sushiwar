
package sushiwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import player.Player;
import scenario.Terrain;
import sound.Music;
import sound.Sound;
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
	
	//	--	Janela  --
	public	int					width;
	public	int					height;
	private	Main				frame			= null;
	private	Terrain				terrain			= null;
	private Image				background;
	//	--	Jogadores  --
    private Player				playerActive	= null;
	private int					playerActiveId	= 0;
    private ArrayList<Player>	playerList		= null;
	//	--	Niguiris  --
    private Niguiri				niguiriActive	= null;
	private ArrayList<Niguiri>	niguiriList		= null;
	//	--	Projéteis  --
	private	ArrayList<Missile>	missileList		= null;
	//	--	Jogo  --
	private GameStatus			gameStatus		= GameStatus.PLAYER_TURN;
	private Timer				gameTimer;
	private Music				gameMusic;
	private double				shakeMagnitude;
	private Timer				shakeTimer;
	private boolean				gameOver;
	
	public enum GameStatus {
		PLAYER_TURN, MISSILE_FLY, EXPLOSION_TIME, DAMAGE_DEAL, NIGUIRI_DEATH
	}
	
	public Screen( int w, int h, Main frame ) {
		super();
        
		//	--	Inicializar janela  --
		this.frame = frame;
		this.width = w;
		this.height = h;
		
		this.setLayout( null );
		
		setSize(w, h);
		setBackground(SCREEN_DEFAULT_BGCOLOR);
		
		URL url = Screen.class.getResource("/assets/background.png");
		background = new ImageIcon(url).getImage();
		this.setForeground(Color.white);
		
		//	--	Carregar fonte  --
        InputStream is = Screen.class.getResourceAsStream( "/assets/InfoBarFont.ttf");
		try {
			Font theFont = Font.createFont( Font.TRUETYPE_FONT, is );
			setFont( theFont.deriveFont(Font.PLAIN, 20));			
		} catch (FontFormatException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		//	--	Carregar música de jogo  --
		gameMusic = new Music("Jinggle");
		//gameMusic.play();
		
		//	--	Inicializar terreno  --
		//terrain = new Terrain(land, this);
		
		//	--	Inicializar niguiris --
		missileList = new ArrayList<Missile>(0);
		niguiriList = new ArrayList<Niguiri>(0);
		
		//	--	Inicializer jogadores  --

		playerList = new ArrayList<Player>(0);
		//for(int i=0; i<numPlayers; i++) {
		//	playerList.add( new Player( numNiguiris, this ) );
		//}
		
		//playerActiveId = 0;
        //playerActive = playerList.get(0);
        //playerActive.toggle( true );

		//for (Player p: playerList)
		//	p.startNiguiri();
		
		//	--	Inicializar controle de estado de jogo  --
		//gameTimer = new Timer( new TimerControl(), 250 );
		//gameTimer.start();
		
		//	--	Inicializar controle de câmera  --
		//shakeTimer = new Timer( new ShakeControl(), 100 );
		//shakeTimer.start();
		
	}
	
	public void startGame( int playerCount, String land ) {
		terrain = new Terrain(land, this);
		
		int niguiriCount = 1;//0/playerCount;
		for(int i=0; i<playerCount; i++) {
			playerList.add( new Player( niguiriCount, this ) );
		}
		
		playerActiveId = 0;
        playerActive = playerList.get(0);
        playerActive.toggle( true );
		
		for (Player p: playerList)
			p.startNiguiri();
		
		//gameMusic.start();
		
		gameTimer = new Timer( new TimerControl(), 250 );
		gameTimer.start();
		
		shakeTimer = new Timer( new ShakeControl(), 100 );
		shakeTimer.start();
	}
	
	public void clearGame() {
		niguiriList.clear();
		playerList.clear();
		missileList.clear();
		gameTimer.finish();
		shakeTimer.finish();
		Player.resetPlayerCount();
		
		gameOver = false;
		gameStatus = GameStatus.PLAYER_TURN;
	}
	
	public void showGame() {
		setVisible(true);
		gameMusic.play();
	}
	
	public void hideGame() {
		setVisible(false);
		gameMusic.halt();
	}
	
	//	--	Controle de agentes  --
	
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
		if (player == playerActive) {
			playerActiveId = ( playerActiveId + 1 ) % playerList.size();
			playerActive = playerList.get( playerActiveId );
		}
		playerList.remove(player);
		
		for ( int i = 0; i < playerList.size(); i++ )
			if (playerList.get(i) == playerActive)
				playerActiveId = i;
	}
	
	//	--	Controle de status de jogo  --
	
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
	
	public void pauseTurn( boolean pause ) {
		playerActive.toggle( !pause );
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
	
	public void nextTurn() {
				
		if ( !playerList.isEmpty()) {
			playerActive.toggle(false);
			playerActiveId = ( playerActiveId + 1 ) % playerList.size();
			playerActive = playerList.get( playerActiveId );
			playerActive.nextNiguiri();
			playerActive.toggle( true );
			setGameStatus( GameStatus.PLAYER_TURN );
		}
		
	}
	
	//	--	Informações  --
	
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
	
	//	--	Atualização  --
	
	public void update() {
		
		//	--	Estado: turno do jogador
		if ( gameStatus == GameStatus.PLAYER_TURN ) {
			
		}
		
		//	--	Estado: explosões e niguiris voando!
		else if ( gameStatus == GameStatus.EXPLOSION_TIME ) {
			if (!checkMovement()) {
				setGameStatus( GameStatus.DAMAGE_DEAL );
			
				try {
					Timer.sleep(2000);
				} catch (InterruptedException ex) {	}
			}
		}
		
		//	--	Estado: dano sendo causado
		else if ( gameStatus == GameStatus.DAMAGE_DEAL ) {
			int totalDamage = 0;
			for (Niguiri n: niguiriList )
				totalDamage += n.doDamage();
			
			if (totalDamage > 0)
				new Sound("Pain").play();
			
			try {
				Timer.sleep(2000);
			} catch (InterruptedException ex) {	}
			
			setGameStatus( GameStatus.NIGUIRI_DEATH );
		}
		
		//	--	Estado: niguiris morrendo
		else if ( gameStatus == GameStatus.NIGUIRI_DEATH ) {
			boolean hasDeath = false;
			for (Niguiri n: niguiriList )
				if (n.getLife() == 0) {
					n.kill();
					hasDeath = true;
				}
			
			if (hasDeath)
				new Sound("Death").play();
			
			try {
				Timer.sleep(2000);
			} catch (InterruptedException ex) { }
			
			//	Um jogador venceu
			if(playerList.size() == 1){
                JOptionPane.showMessageDialog(frame, "Jogador " + (playerList.get(0).getId()+1) + " venceu!", "Game Over",
												JOptionPane.INFORMATION_MESSAGE, null);
				gameOver = true;
            }
			//	Empate
			else if (playerList.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Empate!", "Game Over",
												JOptionPane.INFORMATION_MESSAGE, null);
				gameOver = true;
			}
			
			if (!gameOver)
				nextTurn();
			else {
				frame.resetGame();
			}
		}
	}
	
	public void updateShake() {
		shakeMagnitude /= 2;
		if (shakeMagnitude < 3)
			shakeMagnitude = 0;
	}
	
	//	--	Classes de controle de eventos  --

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
	
	//	--	Controle de gráfico  --
	
	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		g.drawImage( background, 0, 0, null );
		
		double sx = Math.random()*shakeMagnitude - shakeMagnitude/2;
		double sy = Math.random()*shakeMagnitude - shakeMagnitude/2;

		if (terrain != null)
			terrain.print(g, sx, sy);
		
		for (Player p: playerList) {
			p.printNiguiri(g, sx, sy);
		}
		
		for (Niguiri n: niguiriList) {
			n.printCrosshair(g, sx, sy);
		}
		
        for (Missile m: missileList)
			m.print(g);

	}

	
}
