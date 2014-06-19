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

/* SolveCheck.java
 * @ Mia Vanderwilt
 * @ 06/18/14
 * <p>
 * A JFrame that pops up when a player 'solves' for a tile by double clicking on the respective JButton
 * at this point in the game, if a player solves the game ends and they win or lose depending upon 
 * whether or not their guess was the secret square. 
 * <p>
 * The window consists of a question of whether or not the user wants to solve, and a "Yes" or "No" 
 * button, also a visual representation of the guessed tile. If a user chooses to solve (pressing Yes)
 * then a sentence shows declaring what the secret square was and whether or not the user won or lost.
 *  
 */
		
public class SolveCheck {
	
	private JFrame window;
	
	private Tile guess;
	private JLabel question;
	private JButton yes;
	private JButton no;
	
	private static Tile SECRETSQUARE;
	public boolean solve;
	
	public SolveCheck (Tile tile, Font f, Tile sS) {
		guess = tile;
		SECRETSQUARE = sS;
		
		window = new JFrame();
		window.setLayout(new FlowLayout());
		window.setIconImage(new ImageIcon("telepathyIcon.jpg").getImage());
		window.setSize(375, 150);
		window.setLocation(new Point(700, 200));
		
		question = new JLabel("Are you sure you want to solve for " + guess.toString() + "?");
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
				window.add(new JLabel("The Secret Square Was: " + SECRETSQUARE.row + SECRETSQUARE.column));
				JLabel winLose = new JLabel();
				if (guess.toString().equals(SECRETSQUARE.toString())){
					winLose.setText("You WON!");
				} else {
					winLose.setText("You LOST!");
				}
				window.add(winLose);
				window.pack();//TODO: imperfect sizing and location require a user to move and resize the window
			} else {
				solve = false;
			}
			System.out.println(solve);
		}
	}
	
	public JFrame getJFrame() {
		return window;
	}
	
	/* Supposed to indicate to the Board class whether or not the user wants to solve, doesn't work*/
	public boolean getToSolve(){//TODO: Doesn't have intended functionality
		System.out.println(solve);
		return solve;
	}
}
