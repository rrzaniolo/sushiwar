package sushiwar;

import button.ButtonAction;
import button.NiguiriButton;
import com.sun.media.controls.ActionControl;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import sound.Music;

/**
 *
 * @author Daron Vardmir
 */
public class MenuScreen extends JPanel implements Constants {
    public	int                 width;
	public	int                 height;
    private int                 numberOfPlayer;
	private	JFrame              frame			= null;
	private Screen              scr             = null;
    private Music               menuMusic       = null;
    private NiguiriButton       start           = null;
    private NiguiriButton       exit            = null;
    private Image				background;
    
    private int					mouseX;
	private int					mouseY;
    
    
    public MenuScreen(int windowWidth, int windowHeight, String music, JFrame frame){
        
        // -- Inicializando Som --
        this.menuMusic = new Music("MushishiOP");
        menuMusic.start();
        
        // -- Inicializar janela --
        //this.setName("Suschi War");
        
        URL url = MenuScreen.class.getResource("/assets/MenuImage.jpg");
		background = new ImageIcon(url).getImage();
        
        this.setVisible(true);
        this.setLayout( null );
        this.addMouseListener(new MouseControl());
        this.addMouseMotionListener(new MotionControl());
		
        start = new NiguiriButton( (windowWidth - 150)/2, (windowHeight - 45)/2, 150, "START",this );
        start.setAction( new StartActionControl() );
        
        exit = new NiguiriButton( (windowWidth - 150)/2,(windowHeight - 45)/2 + 45 , 150, "SAIR", this );
        exit.setAction( new ExitActionControl() );
		
		this.frame = frame;
		this.width = windowWidth;
		this.height = windowHeight;
		setSize(windowWidth, windowHeight);
		setBackground(MENU_DEFAULT_BGCOLOR);
				
		this.setForeground(Color.white);
                        
    }
    
    public void setMenuVisible(boolean visuability){
        this.setVisible(visuability);
    }
    
    class MouseControl extends MouseAdapter {
		
        @Override
		public void mousePressed( MouseEvent e ) {
			
		}
		
	}
	
	class MotionControl extends MouseMotionAdapter {
		
        @Override
		public void mouseMoved( MouseEvent e ) {
			MenuScreen.this.mouseX = e.getX();
			MenuScreen.this.mouseY = e.getY();
		}
		
	}
    
    private class StartActionControl extends ButtonAction {

		@Override
        public void execute() {
            scr = new Screen(800,600, frame);
            setMenuVisible(false);
            menuMusic.halt();
            frame.add(scr);
            scr.setVisible(true);
		}
		
	}
    
    private class ExitActionControl extends ButtonAction {

		@Override
        public void execute() {
            System.exit(0);
		}
		
	}
    
    @Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		g.drawImage( background, 0, 0, null );
		
        start.print(g);
        exit.print(g);
		
	}

}



