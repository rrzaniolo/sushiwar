package sushiwar;

import button.ButtonAction;
import button.NiguiriButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sound.Music;

/**
 *
 * @author Daron Vardmir
 */
public class MenuScreen extends JPanel implements Constants {
    public	int                 width;
	public	int                 height;
    private int                 numberOfPlayers;
	private	Main	            frame			= null;
	private Screen              scr             = null;
    private Music               menuMusic       = null;
    private NiguiriButton       start           = null;
    private NiguiriButton       exit            = null;
	private NiguiriButton       resume          = null;
    private Image				background;
    
    
    public MenuScreen(int windowWidth, int windowHeight, String music, Main frame){
		
		//	--	Inicializando janela  --
        URL url = MenuScreen.class.getResource("/assets/MenuImage.png");
		background = new ImageIcon(url).getImage();
        
		this.frame = frame;
		this.width = windowWidth;
		this.height = windowHeight;
		setSize(windowWidth, windowHeight);
		setBackground(MENU_DEFAULT_BGCOLOR);
				
		this.setForeground(Color.white);
		
        this.setVisible(true);
        this.setLayout( null );
		
		//	--	Inicializando fonte  --
        InputStream is = MenuScreen.class.getResourceAsStream( "/assets/InfoBarFont.ttf");
		try {
			Font theFont = Font.createFont( Font.TRUETYPE_FONT, is );
			setFont( theFont.deriveFont(Font.PLAIN, 20));			
		} catch (FontFormatException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		// -- Inicializando música --
		
		//	--	Inicializando botões  --		
        /*start = new NiguiriButton( (windowWidth - 150)/2, (windowHeight - 45)/2, 150, "START",this );
        start.setAction( new StartActionControl() );
        
        exit = new NiguiriButton( (windowWidth - 150)/2,(windowHeight - 45)/2 + 45 , 150, "SAIR", this );
        exit.setAction( new ExitActionControl() );*/
		start = new NiguiriButton( (windowWidth - 250), (windowHeight - 150), 200, "NOVO JOGO", this );
        start.setAction( new StartActionControl() );
        
        exit = new NiguiriButton( (windowWidth - 250),(windowHeight - 100) , 200, "SAIR", this );
        exit.setAction( new ExitActionControl() );
		
		resume = new NiguiriButton( (windowWidth - 250),(windowHeight - 200) , 200, "RETORNAR", this );
        resume.setAction( new ResumeActionControl() );
		resume.setVisible( false );
                        
    }
    
    public void showMenu (){
        this.setVisible(true);
		//this.menuMusic = new Music("MushishiOP");
		//menuMusic.play();
    }  
    
	public void hideMenu() {
		this.setVisible(false);
		if (menuMusic != null)
			menuMusic.halt();
	}
	
	public void canResume( boolean can ) {
		resume.setVisible(can);
	}
	
    private class StartActionControl extends ButtonAction {

		@Override
        public void execute() {
            SubMenu submenu = new SubMenu();
            int nump = submenu.getNumberOfPlayer();
            SubMenu2 submenu2 = new SubMenu2();
            String land = submenu2.getLand();
            
            frame.startGame( nump, land );
		}
		
	}
    
    private class ExitActionControl extends ButtonAction {

		@Override
        public void execute() {
            System.exit(0);
		}
		
	}
    
	private class ResumeActionControl extends ButtonAction {

		@Override
        public void execute() {
            frame.toggleMenu(false);
		}
		
	}
	
    @Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		g.drawImage( background, 0, 0, null );
		
        start.print(g);
        exit.print(g);
		resume.print(g);
		
	}
    
    class SubMenu extends JOptionPane{
        private int numberOfPlayers;     
        private Object [] ops = {"2 Jogadores","3 Jogadores","4 Jogadores"}; 

        public SubMenu(){
            do{
            numberOfPlayers = showOptionDialog(frame, "Escolha o Número de Jogadores", "Jogadores", 
                    DEFAULT_OPTION, QUESTION_MESSAGE, icon, ops, null);
            System.out.println(numberOfPlayers);
            }while(numberOfPlayers <0);
            
            numberOfPlayers = numberOfPlayers +2;
        }
        
        public int getNumberOfPlayer(){
            return(numberOfPlayers);
        }
    }
    
    class SubMenu2 extends JOptionPane{
        private String land;
        private int indice;
        private Object [] ops = {"Colina","Desfiladeiro","Vale das Trevas","Geometria","Planície","Ilhas Celestes","Tempestade"}; 

        public SubMenu2(){
            do {
             this.indice = showOptionDialog(frame, "Escolha o Terreno no qual deseja jogar", "Terreno", 
                    DEFAULT_OPTION, QUESTION_MESSAGE, icon, ops, null);
            } while(indice <0);
            
            this.land = (String)(ops[indice]);
        }
        
        public String getLand(){
            return(land);
        }
    }
}
