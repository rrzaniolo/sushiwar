/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sushiwar;

import menu.MenuPrincipal;
import javax.swing.JFrame;
import menu.MenuJogo;

/**
 * @author Hossomi
 * 
 * CASS Main ----------------------------------------------
 * Classe principal. É a janela!
 */

public class Main extends JFrame {
	
	private static MenuJogo	gameMenu;
	private static MenuPrincipal mainMenu;
	private static Screen game;
	
	public enum MenuStatus {
		MENU_MAIN, MENU_GAME, MENU_NONE;
	}
	
	public Main() {
		super("Sushi War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public void startGame( int playerCount, String land ) {
		game.startGame( playerCount, land );
		toggleMenu( MenuStatus.MENU_NONE );
	}
	
	public void resetGame() {
		game.clearGame();
		game.hideGame();
		mainMenu.showMenu();
		mainMenu.canResume(false);
	}
	
	public void toggleMenu( MenuStatus which ) {
		if ( which == MenuStatus.MENU_MAIN) {
			game.hideGame();
			gameMenu.hideMenu();
			mainMenu.showMenu();
			mainMenu.canResume(true);
		}
		else if ( which == MenuStatus.MENU_GAME) {
			game.hideGame();
			gameMenu.showMenu();
			mainMenu.hideMenu();
		}
		else {
			game.showGame();
			gameMenu.hideMenu();
			mainMenu.hideMenu();
		}
	}
	
	public static void main( String []args ) {
		Main frame = new Main();
		
        mainMenu = new MenuPrincipal(800, 600,"MushishiOP", frame);
		gameMenu = new MenuJogo( 800, 600, frame );
		game = new Screen( 800, 600, frame );
		frame.add(mainMenu);
		frame.add(gameMenu);
		frame.add(game);
		game.hideGame();
		gameMenu.setVisible(false);
		mainMenu.showMenu();
//        Screen scr = new Screen(800,600, frame);
//        frame.add(scr);
		
		//frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize( 800 + frame.getInsets().left + frame.getInsets().right , 600 + frame.getInsets().top + frame.getInsets().bottom);
		
		//frame.addMouseListener( scr );
	}
	
}
