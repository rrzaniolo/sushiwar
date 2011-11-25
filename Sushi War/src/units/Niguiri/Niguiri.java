
package units.Niguiri;

/**
 * @author Hossomi
 * 
 */

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import player.DirPad;
import player.DirPad.Direction;
import player.Player;
import sound.Sound;
import sushiwar.Constants;
import sushiwar.Screen;
import sushiwar.Screen.GameStatus;
import units.Unit;
import units.missile.ExplodingSushi;
import units.missile.PowerBar;

public class Niguiri extends Unit implements Constants {

	//	--	Movimento  --
	private NiguiriStatus	status		= null;
	
	//	--	Configurações  --
	private Player			player		= null;
	private Crosshair		crosshair	= null;
	private int				life		= 0;
	private int				damageTaken = 0;
	private String			name		= null;
	private InfoBar			infoBar		= null;
	private PowerBar		powerBar	= null;
	
	private static int		niguiriCount= 0;
	
	public enum NiguiriStatus {
		STAND, WALK, JUMP, FALL, LAND, DIZZY, FIRE, CRY, DIE;
	}
	
	//	-----------------------------------------------------------------------

	/**
	 * Personagem controlado pelo jogador. Responde a todos os comandos básicos
	 * como setas direcionais, pulo e barra de espaço.
	 * 
	 * @param x Posição X do Oniguiri
	 * @param y Posição Y do Oniguiri
	 * @param player Jogador controlador do Oniguiri
	 * @param screen Tela do jogo
	 * @param respondControl Se verdadeiro, o Oniguiri responderá aos controles
	 *	desde o início
	 */
	public Niguiri( double x, double y, Player player, Screen screen, boolean respondControl ) {
		super(x, y, 30, 30, screen, respondControl );
		setCollisionBox( 7, 9, 16, 16 );
		this.player = player;
		this.life = NIGUIRI_INITIAL_LIFE;
		
		sprite = new NiguiriSprite( screen );
		status = NiguiriStatus.STAND;
		
		//	--	Crosshair  --
		crosshair = new Crosshair( this, screen );
		
		//	--	Name  --
		name = "Niguiri " + player.getId() + ":" + niguiriCount;
		niguiriCount++;
		
		infoBar = new InfoBar( this, screen );
				
		//	--	Power bar  --
		powerBar = new PowerBar( this, screen );
		
		//	--	Stuff  --
		screen.addNiguiri( this );
	}
	
	//	--  Manipulação  ------------------------------------------------------
	
	/**
	 * Define o status atual do Oniguiri. Seu status determina se ele responde
	 * aos comandos, se pode atirar ou se pode movimentar.
	 * @param now Status atual
	 */
	public void setStatus( NiguiriStatus now ) {
		if (status != now) {
			blockMovement = false;
			status = now;

			if (now == NiguiriStatus.WALK) {
				playAnimation("walk");
				infoBar.setVisible(false);
				ready = true;
			}
			else if (now == NiguiriStatus.JUMP) {
				playAnimation("jump");
				infoBar.setVisible(false);
				ready = false;
			}
			else if (now == NiguiriStatus.FALL) {
				infoBar.setVisible(false);
				ready = false;
			}
			else if (now == NiguiriStatus.LAND) {
				infoBar.setVisible(false);
				playAnimation("land");
				ready = false;
			}
			else if (now == NiguiriStatus.STAND) {
				infoBar.setVisible(true);
				playAnimation("stand");
				ready = true;
			}
			else if (now == NiguiriStatus.DIZZY) {
				infoBar.setVisible(false);
				playAnimation("dizzy");
				ready = false;
			}
			else if (now == NiguiriStatus.FIRE) {
				infoBar.setVisible(false);
				playAnimation("fire");
				blockMovement = true;
				ready = false;
			}
			else if (now == NiguiriStatus.CRY) {
				infoBar.setVisible(false);
				playAnimation("cry");
				ready = false;
			}
			else if (now == NiguiriStatus.DIE) {
				infoBar.setVisible(true);
				playAnimation("die");
				ready = false;
			}
		}
	}
	
	/**
	 * Modifica a vida do Oniguiri. Reduzir a zero não o mata automaticamente.
	 * @param life Nova vida do Oniguiri
	 */
	public void setLife( int life ) {
		this.life = life;
	}
	
	/**
	 * Registra dano recebido pelo Oniguiri. Não reduz sua vida automaticamente.
	 * @param damage Dano recebido
	 */
	public void applyDamage( int damage ) {
		damageTaken += damage;
	}
	
	/**
	 * Efetua o dano registrado, deduzindo-o da sua vida. Não mata automatiamente
	 * e zera o dano registrado.
	 * @return 
	 */
	public int doDamage() {
		int damage = damageTaken;
		life = Math.max( life -= damageTaken, 0 );
		damageTaken = 0;
		
		return damage;
	}
	
	/**
	 * Mata o Oniguiri. Pode ser cancelado alterando seu status.
	 */
	public void kill() {
		setStatus( NiguiriStatus.DIE );
	}
	
	/**
	 * Atira um sushi explosivo com uma determinada potência.
	 * @param power Potência do tiro (em %)
	 */
	public void fire( int power ) {
		ExplodingSushi sushi = new ExplodingSushi( this, power, screen );
		powerBar.toggle(false);
		setStatus( NiguiriStatus.STAND );
		
		screen.setGameStatus( Screen.GameStatus.MISSILE_FLY );
	}
	
	/**
	 * Ativa ou desativa o Oniguiri. Um Oniguiri desativado não responde a nenhum
	 * comando.
	 * @param on Se verdadeiro, o Oniguiri é ativado
	 */
	public void toggle( boolean on ) {
		super.toggle(on);
	}
	
	/**
	 * Realiza atualizações de status do Oniguiri dependendo de sua posição ou
	 * status atual.
	 * @return 
	 */
	public int update() {
		int updateStatus = super.update();
		boolean hitGround = (updateStatus & Constants.MOVE_HITGROUND_VERTICAL) != 0;
		
		if (status == NiguiriStatus.DIE) {
			if (sprite.isDone()) {
				remove();
				screen.explode( getPositionX(), getPositionY(), 20, 40, 30 );
			}
		}
		
		else if (onAir && !hitGround) {
			if (damageTaken == 0)
				setStatus( NiguiriStatus.JUMP );
			else
				setStatus( NiguiriStatus.DIZZY );
		}
		
		if (hitGround && (status == NiguiriStatus.JUMP || status == NiguiriStatus.DIZZY) ) {
			setStatus( NiguiriStatus.LAND );
		}
		
		if ( status == NiguiriStatus.LAND ) {
			if (sprite.isDone()) {
				if (isMoving())
					setStatus( NiguiriStatus.WALK );
				else
					setStatus( NiguiriStatus.STAND );
			}
		}
		
		int boxStatus = screen.isBoxInScreen(box);
		if ((boxStatus & 0x01011) != 0 && (boxStatus &0x10000) > 0) {
			if (screen.getGameStatus() == GameStatus.PLAYER_TURN)
				screen.setGameStatus( GameStatus.EXPLOSION_TIME );
			remove();
			new Sound("Aaaaah").play();
		}
		
		if (ready) {
			if (controlPad.isDirectionPressed( Direction.DOWN ))
				crosshair.changeAngle(-1);
			
			if (controlPad.isDirectionPressed( Direction.UP ))
				crosshair.changeAngle(1);
		}
		
		infoBar.update();
		crosshair.update();
		
		return 0;
	}
	
	/**
	 * Remove o Oniguiri de jogo.
	 */
	public void remove() {
		player.removeNiguiri(this);
		moveTimer.finish();
		sprite.remove();
		screen.removeNiguiri(this);
		
		powerBar.remove();
		infoBar.remove();
	}
	
	//	--  Informação  -------------------------------------------------------
	
	/**
	 * Retorna o controlador deste Oniguiri.
	 * @return O controlador do Oniguiri
	 */
	public Player getPlayer(){
        return this.player;
    }
	
	/**
	 * Retorna o id do controlador deste Oniguiri.
	 * @return O id do controlador do Oniguiri
	 */
	public int getPlayerId() {
		return player.getId();
	}
	
	/**
	 * Retorna o nome do Oniguiri.
	 * @return O nome do oniguiri
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retorna a vida do Oniguiri.
	 * @return A vida do Oniguiri
	 */
	public int getLife() {
		return life;
	}
	
	/**	
	 * Retorna o ponto de onde o tiro deve partir.
	 * @return O X do ponto de saída do tiro
	 */
	public double getFireX() {
		return x + Constants.NIGUIRI_FIRE_RADIUS * Math.cos( crosshair.getAngle() );
	}
	
	/**	
	 * Retorna o ponto de onde o tiro deve partir.
	 * @return O Y do ponto de saída do tiro
	 */
	public double getFireY() {
		return y + Constants.NIGUIRI_FIRE_RADIUS * Math.sin( crosshair.getAngle() );
	}
	
	/**
	 * Retorna o ângulo em que o Oniguiri está mirando.
	 * @return O ângulo da mira
	 */
	public double getFireAngle() {
		return crosshair.getAngle();
	}
	
	/**
	 * Retorna a quantidade de dano recebido registrado no Oniguiri.
	 * @return O dano registrado
	 */
	public int getDamageTaken() {
		return damageTaken;
	}
	
	public String toString() {
		return name;
	}
	
	//	--  Eventos  ----------------------------------------------------------
	
	@Override
	public void keyPressedOnce( KeyEvent e ) {
		super.keyPressedOnce(e);

		if (status != NiguiriStatus.FIRE) {
			if (!onAir && isMoving())
				setStatus(NiguiriStatus.WALK);

			//	--	Pulo normal  --
			if ( ready && e.getKeyCode() == MOVE_NIGUIRI_JUMP_KEY ) {
				setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_JUMP_VX, -MOVE_NIGUIRI_JUMP_VY );
				setStatus(NiguiriStatus.JUMP);
			}

			//	--	Pulo alto  --
			else if ( ready && e.getKeyCode() == MOVE_NIGUIRI_HJUMP_KEY ) {
				setSpeed( DirPad.Direction2X(facing)*MOVE_NIGUIRI_HJUMP_VX, -MOVE_NIGUIRI_HJUMP_VY );
				setStatus(NiguiriStatus.JUMP);
			}
			
			else if ( status == NiguiriStatus.STAND && e.getKeyCode() == KeyEvent.VK_SPACE) {
				setStatus( NiguiriStatus.FIRE );
				powerBar.reset();
				powerBar.toggle(true);
			}
		}
		
	}
	
	@Override
	public void keyReleasedOnce( KeyEvent e ) {
		super.keyReleasedOnce(e);
		
		if ( status == NiguiriStatus.FIRE && e.getKeyCode() == KeyEvent.VK_SPACE ) {
			fire( powerBar.getPercentage() );
		}
		
		if (status != NiguiriStatus.FIRE && !isMoving())
			this.setStatus(NiguiriStatus.STAND);
		
	}
	
	//	--  Gráfico  ----------------------------------------------------------
	
	/**
	 * Imprime num contexto gráfico.
	 * @param g Contexto gráfico
	 */
	@Override
	public void print( Graphics g ){
		super.print(g);
	}
	
	/**
	 * Imprime num contexto gráfico com um deslocamento.
	 * @param g Contexto gráfico
	 * @param sx Deslocamento em X
	 * @param sy Deslocamento em Y
	 */
	public void print( Graphics g, double sx, double sy ){
		super.print(g, sx, sy);
	}
	
	/**
	 * Imprime num contexto gráfico.
	 * @param g Contexto gráfico
	 */
	public void printCrosshair( Graphics g ) {
		if (respondControl && status == NiguiriStatus.STAND )
			crosshair.print(g);
	}
	
	/**
	 * Imprime num contexto gráfico com um deslocamento.
	 * @param g Contexto gráfico
	 * @param sx Deslocamento em X
	 * @param sy Deslocamento em Y
	 */
	public void printCrosshair( Graphics g, double sx, double sy ) {
		if (respondControl && status == NiguiriStatus.STAND )
			crosshair.print(g, sx, sy);
	}
       
}
