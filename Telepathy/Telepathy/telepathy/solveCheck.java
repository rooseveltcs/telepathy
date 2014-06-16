package telepathy;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;

public class solveCheck {
	
	private JFrame window;
	
	private JLabel question;
	private JButton yes;
	private JButton no;
	
	private Tile secretSquare;//
	public boolean solve;
	
	public solveCheck (Tile tile, Font f, Tile sS) {
		
		secretSquare = sS;
		
		window = new JFrame();
		window.setLayout(new FlowLayout());
		window.setIconImage(new ImageIcon("telepathyIcon.jpg").getImage());
		window.setSize(375, 150);
		window.setLocation(new Point(700, 200));
		
		question = new JLabel("Are you sure you want to solve for " + tile.toString() + "?");
		question.setFont(f);
		
		yes = new JButton("Yes");
		yes.setName("Yes");
		yes.addMouseListener(new solveListener());//TODO: Potentially get rid of all the mousemotionlisteners 
		yes.setFont(f);
		
		no = new JButton("No");
		no.addMouseListener(new solveListener());
		no.setFont(f);
		
		window.add(question);
		window.add(yes);
		window.add(no);
		
		JLabel image = tile.imageRep;
		image.setSize(500, 500);
		window.add(image);
		
		window.setVisible(true);
	}
	
	public class solveListener extends MouseInputAdapter {//Should this be private? TODO:
		
		private boolean contToSolve;
			
		public void mousePressed (MouseEvent event){
			JButton b = (JButton) event.getComponent();
			System.out.println(b.getText());
			if (b.getName().equals("Yes")) { 
				solve = true;
				window.add(new JLabel("The Secret Square Was: " + secretSquare.row + secretSquare.column));
			} else {
				solve = false;
			}
			//window.setVisible(false);
			System.out.println(solve);
		}
	}
	
	public JFrame getJFrame() {
		return window;
	}
	
	public boolean getToSolve(){
		System.out.println(solve);
		return solve;
	}
}
