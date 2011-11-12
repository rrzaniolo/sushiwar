package units.missile;

import sushiwar.Constants;
import sushiwar.Screen;
import units.Unit;

/**
 *
 * @author Hossomi
 */
public abstract class Missile extends Unit {
	
	private		int			damage;
	private		double		explosionRadius;
	protected	double		explosionPower;
	
	protected static final double	MAX_SPEED = 50;
	
	public Missile( double x, double y, int w, int h, int damage, int explosionRadius, int explosionPower, Screen screen ) {
		super( x, y, w, h,  screen );
		setCollisionBoxCenter( 5, 5 );
		
		this.damage = damage;
		this.explosionRadius = explosionRadius;
		this.explosionPower = explosionPower;
	}
	
	public int update() {
		int updateStatus = super.update();
		
		if (updateStatus > 0)
			explode();
		
		return updateStatus;
	}
	
	public void explode() {
		screen.explode( x, y, damage, explosionRadius, explosionPower );
		screen.removeMissile(this);
		this.moveTimer.finish();
	}
}
