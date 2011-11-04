package units.missile;

import sushiwar.Constants;
import sushiwar.Screen;
import units.Unit;

/**
 *
 * @author Hossomi
 */
public abstract class Missile extends Unit {
	
	private int			damage;
	private int			explosionRadius;
	
	public Missile( int x, int y, int w, int h, int damage, int explosionRadius, Screen screen ) {
		super( x, y, w, h,  screen );
		this.damage = damage;
		this.explosionRadius = explosionRadius;
	}
	
	public int update() {
		int updateStatus = super.update();
		
		if (updateStatus > 0)
			explode();
		
		return updateStatus;
	}
	
	public void explode() {
		screen.terrain.explode( x, y, explosionRadius );
		screen.missileList.remove(this);
		this.moveTimer.finish();
	}
}
