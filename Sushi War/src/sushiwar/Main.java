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
	
	public Main() {
		super("Sushi War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main( String []args ) {
		Main frame = new Main();
		
		Screen scr = new Screen(800,600, frame);
		frame.getContentPane().add(scr);
		
		//frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setSize( 800 + frame.getInsets().left + frame.getInsets().right , 600 + frame.getInsets().top + frame.getInsets().bottom);
		
		frame.addKeyListener( scr );
	}
	
}
