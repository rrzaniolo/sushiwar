/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sushiwar;

import java.awt.event.KeyEvent;

/**
 * @author Hossomi
 * 
 * INTERFACE Constants --------------------------------------------------------
 * Contém todas as constantes do jogo! Uol!
 */
public interface Constants {
	
	//	--	Units stuff  --
	static final int	MOVE_TIMER_PERIOD		= 10;
	static final int	MOVE_CLIFF_TOLERANCE	= 6;
	static final int	MOVE_FALLING_HEIGHT		= 5;
	static final double MOVE_NIGUIRI_SPEED		= 0.5;
	
	static final int	MOVE_NIGUIRI_JUMP_KEY	= KeyEvent.VK_SPACE;
	
	static final double GRAVITY					= 0.15;
	
	//	--	Sprite stuff  --
	static final int	SPRITE_TIMER_PERIOD		= 10;
	
	//	--	Other stuff  --
	static final int	SCREEN_OUT_RIGHT		= 0x00001;
	static final int	SCREEN_OUT_LEFT			= 0x00010;
	static final int	SCREEN_OUT_TOP			= 0x00100;
	static final int	SCREEN_OUT_BOTTOM		= 0x01000;
	static final int	SCREEN_OUT_TOTAL		= 0x10000;
}
