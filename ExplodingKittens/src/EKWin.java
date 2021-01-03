/*
 * Nicholas Mair
 * December 2018 - 
 * Window for Exploding Kittens
 */ 
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class EKWin extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public EKWin() {
		super("Exploding Kittens");
		ImageIcon img = new ImageIcon("assets/icon.png");
		setContentPane(new ExplodingKittensGame());//Essentially Canvas
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Necessary to close window
        setSize(1500,1000);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
	}
	
	public static void main(String[] args) {
		new EKWin();
	}
	

}