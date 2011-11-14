package sushiwar;

import java.util.ArrayList;
import player.Player;
import units.Niguiri.Niguiri;

/**
 *
 * @author Hossomi
 */
public class GameController implements Constants {
	private	ArrayList<Player>	playerList;
	private ArrayList<Niguiri>	niguiriList;
	private int					currentPlayer;
	
	public GameController( int playerCount ) {
		
		playerList = new ArrayList<Player> (playerCount);
		niguiriList = new ArrayList<Niguiri> (playerCount * PLAYER_NIGUIRI_COUNT );
		
	}
}
