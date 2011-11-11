package units.missile;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeListener;
import sushiwar.Constants;
import sushiwar.Screen;
import timer.Timer;
import timer.TimerListener;
import units.Niguiri.Niguiri;

/**
 *
 * @author Hossomi
 */
public class PowerBar implements Constants {
	
	private JProgressBar		bar;
	private Niguiri				niguiri;
	private int					percent;
	private Timer				timer;
	
	private static final int	POWERBAR_WIDTH	= 100;
	private static final int	POWERBAR_HEIGHT	= 5;
	
	public PowerBar( Niguiri niguiri, Screen screen ) {
		//model = new BoundedRangeModel()
		bar = new JProgressBar(0, 100);
		bar.setValue(0);
		bar.setBounds( (int) niguiri.getPositionX() - POWERBAR_WIDTH/2,
					   (int) niguiri.getPositionY() + 50,
					   POWERBAR_WIDTH,
					   POWERBAR_HEIGHT );
		screen.add( bar );
		bar.setVisible(false);
		
		percent = 0;
		timer = new Timer( new TimerControl(), 20 );
		
		this.niguiri = niguiri;
	}
	
	public void update() {
		bar.setBounds( (int) niguiri.getPositionX() - POWERBAR_WIDTH/2,
					   (int) niguiri.getPositionY() - 30,
					   POWERBAR_WIDTH,
					   POWERBAR_HEIGHT );
		
		if (percent < 100) {
			percent += 1;
			bar.setValue(percent);
		}
	}
	
	public void toggle( boolean on ) {
		bar.setVisible(on);
		
		if (timer.isAlive())
			timer.pause(!on);
		else if (on)
			timer.start();
	}
	
	public void reset() {
		percent = 0;
		bar.setValue(0);
	}
	
	public int getPercentage() {
		return percent;
	}
	
	private class TimerControl implements TimerListener {

		@Override
		public int update() {
			PowerBar.this.update();
			return 0;
		}
		
	}
}
