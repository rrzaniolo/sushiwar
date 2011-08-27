
package timer;

/**
 * @author paulovich
 * Com modificações.
 * 
 * (Roubei!)
 */
public class Timer extends Thread {

    public Timer(TimerListener listener, long millis) {
        this.listener = listener;
        this.millis = millis;
        this.pause = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(millis);

                if (!pause) {
                    listener.update();
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    public void pause( boolean pause ) {
        this.pause = pause;
    }
    
    private boolean pause;
    private long millis;
    private TimerListener listener;
}
