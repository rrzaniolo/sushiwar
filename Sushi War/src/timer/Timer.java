
package timer;

/**
 * @author paulovich
 * Com modificações.
 * 
 * (Roubei!)
 */
public class Timer extends Thread {

    public Timer(TimerListener listener, long period) {
        this.listener = listener;
        this.period = period;
        this.pause = false;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(period);

                if (!pause) {
                    listener.update();
                }
            } catch (InterruptedException ex) {
            }
        }
		stop = false;
    }

    public void pause( boolean pause ) {
        this.pause = pause;
    }
    
	public void finish() {
		stop = true;
	}
	
	public void setPeriod( int period ) {
		this.period = period;
	}
	
    private boolean pause = false;
	private boolean stop = false;
    private long period = 1000;
    private TimerListener listener = null;
}
