/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sushiwar;

import java.awt.Color;
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
	static final int	MOVE_CLIFF_TOLERANCE	= 5;
	static final int	MOVE_FALLING_HEIGHT		= 15;
	static final double MOVE_NIGUIRI_SPEED		= 3;
	
	static final int	MOVE_NIGUIRI_JUMP_KEY	= KeyEvent.VK_Q;
	static final int	MOVE_NIGUIRI_HJUMP_KEY	= KeyEvent.VK_W;
	static final double MOVE_NIGUIRI_JUMP_VX	= 10;
	static final double MOVE_NIGUIRI_JUMP_VY	= 40;
	static final double MOVE_NIGUIRI_HJUMP_VX	= 3;
	static final double MOVE_NIGUIRI_HJUMP_VY	= 55;
	
	static final int	MOVE_HITGROUND_VERTICAL			= 0x01;
	static final int	MOVE_HITGROUND_HORIZONTAL		= 0x10;
	
	static final int    CHANGE_NIGUIRI_KEY      = KeyEvent.VK_C;
	
	static final double GRAVITY					= 8;
	
	//	--	Niguiri stuff  --
	
	static final int	NIGUIRI_WIDTH			= 30;
	static final int	NIGUIRI_INITIAL_LIFE	= 100;
	static final double	CROSSHAIR_RADIUS		= 50;
	static final double CROSSHAIR_ANGLE_MIN		= -Math.PI/2;
	static final double CROSSHAIR_ANGLE_MAX		= Math.PI/3;
	static final double	NIGUIRI_FIRE_RADIUS		= 20;
	
	//	--	Sprite stuff  --
	static final int	SPRITE_TIMER_PERIOD		= 10;
	
	//	--	Player stuff  --
	static final int	PLAYER_NIGUIRI_COUNT	= 1;
    static final int	PLAYER_COUNT            = 4;
	
	//	--	Screen stuff  --
	static final Color	SCREEN_DEFAULT_BGCOLOR	= Color.decode("0x0080ff");
	static final int	SCREEN_OUT_RIGHT		= 0x00001;
	static final int	SCREEN_OUT_LEFT			= 0x00010;
	static final int	SCREEN_OUT_TOP			= 0x00100;
	static final int	SCREEN_OUT_BOTTOM		= 0x01000;
	static final int	SCREEN_OUT_TOTAL		= 0x10000;
}
