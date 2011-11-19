/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sushiwar;

import javax.swing.JFrame;

/**
 * @author Hossomi
 * 
 * CASS Main ----------------------------------------------
 * Classe principal. É a janela!
 */

public class Main extends JFrame {
	
	private static MenuScreen menu;
	private static Screen game;
	
	public Main() {
		super("Sushi War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public void startGame( int playerCount, String land ) {
		game.startGame( playerCount, land );
		menu.hideMenu();
		game.showGame();
	}
	
	public void resetGame() {
		game.clearGame();
		game.hideGame();
		menu.showMenu();
	}
	
	public static void main( String []args ) {
		Main frame = new Main();
		
        menu = new MenuScreen(800, 600,"MushishiOP", frame);
		game = new Screen( 800, 600, frame );
		frame.add(menu);
		frame.add(game);
		game.hideGame();
		menu.showMenu();
//        Screen scr = new Screen(800,600, frame);
//        frame.add(scr);
		
		//frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize( 800 + frame.getInsets().left + frame.getInsets().right , 600 + frame.getInsets().top + frame.getInsets().bottom);
		
		//frame.addMouseListener( scr );
	}
	
}
