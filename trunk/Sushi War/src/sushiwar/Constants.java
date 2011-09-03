/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sushiwar;

/**
 * @author Hossomi
 * 
 * INTERFACE Constants --------------------------------------------------------
 * Contém todas as constantes do jogo! Uol!
 */
public interface Constants {
	static final int TIMER_MOVE_PERIOD = 10;
	static final int TIMER_SPRITE_PERIOD = 10;
	static final int CLIFF_TOLERANCE = 6;
	static final double NIGUIRI_SPEED = 0.5;
	static final double GRAVITY	= 0.1;
	
	static final int SCREEN_OUT_RIGHT = 0x00001;
	static final int SCREEN_OUT_LEFT  = 0x00010;
	static final int SCREEN_OUT_TOP   = 0x00100;
	static final int SCREEN_OUT_BOTTOM= 0x01000;
	static final int SCREEN_OUT_TOTAL = 0x10000;
}
