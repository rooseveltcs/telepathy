package telepathy;
//TODO: perhaps, transfer this to the GuessTable
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
 * whether or not their guess was the secret square. Intended to prevent the game from ending unintentionally.
 * <p>
 * The window consists of a question of whether or not the user wants to solve, and a "Yes" or "No" 
 * button, also a visual representation of the guessed tile. If a user chooses to solve (pressing Yes)
 * then a sentence shows declaring what the secret square was and whether or not the user won or lost.
 * Currently, the user is forced to resize/move the window if they want to see all of the contents, but that will 
 * be fixed.
 */
		
public class SolveCheck {
	
	/* A small window that pops-up in the middle of the screen that asks if the user wants to solve*/
	private JFrame window;
	
	/* A variable intended to track whether or not a user intends to solve, currently doesn't have intended function */
	public boolean solve;
	
	private static Tile GUESS;//Fed to constructor as parameters from Board class
	private static Tile SECRETSQUARE;//Fed to constructor as parameters from Board class
	
	/* Constructor
	 * Creates a JFrame with the question of whether or not to solve, and two buttons: 'Yes' or 'No', each with the same
	 * listener. The solve check window also contains a picture of the tile to be solved for. Shows the frame. 
	 * <p>
	 * @param Tile g - the Tile guessed by the user (to solve for the secret square)
	 * @param Font f - the custom font used in all of the classes; passed to circumvent declaring/initializing again
	 * @param Tile sS - the secret square chosen by the program in the Board class (used to check the guess accuracy) 
	 */
	public SolveCheck (Tile g, Font f, Tile sS) {
		GUESS = g;
		SECRETSQUARE = sS;
		
		window = new JFrame();
		window.setLayout(new FlowLayout());
		window.setIconImage(new ImageIcon("telepathyIcon.jpg").getImage());
		window.setSize(375, 150);
		window.setLocation(new Point(700, 200));
		
		JLabel question = new JLabel("Are you sure you want to solve for " + GUESS.toString() + "?");
		question.setFont(f);
		
		JButton yes = new JButton("Yes");
		yes.setName("Yes");
		yes.addMouseListener(new SolveListener());//TODO: Potentially get rid of all the mouse motion listeners 
		yes.setFont(f);
		
		JButton no = new JButton("No");
		no.addMouseListener(new SolveListener());
		no.setFont(f);
		
		window.add(question);
		window.add(yes);
		window.add(no);
		
		JLabel image = GUESS.imageRep;
		image.setSize(500, 500);
		window.add(image);
		
		window.setVisible(true);//Window currently doesn't close
	}
	
	/* SolveListener.java 
	 * [inner class to SolveCheck]
	 * 
	 * Used for the 'Yes' and 'No' buttons on the solveCheck window. Distinguishes between the two by the text on the button. If a user presses 'Yes' then
	 * the secret square is announced and they find out if they won or lost. Otherwise nothing happens. As of now, the user is forced to close the window on his/her
	 * own. But in the future, pressing no will close the solveCheck window and allow the user to continue with their game.
	 */
	private class SolveListener extends MouseInputAdapter {
		/* Read above class description */
		public void mousePressed (MouseEvent event){
			JButton b = (JButton) event.getComponent();
			System.out.println(b.getText());
			if (b.getName().equals("Yes")) { 
				solve = true;
				window.add(new JLabel("The Secret Square Was: " + SECRETSQUARE.row + SECRETSQUARE.column));
				JLabel winLose = new JLabel();
				if (GUESS.toString().equals(SECRETSQUARE.toString())){
					winLose.setText("You WON!");
				} else {
					winLose.setText("You LOST!");
				}
				window.add(winLose);
				window.pack();//TODO: imperfect sizing and location require a user to move and resize the window
			} else {//If user presses "No" button
				solve = false;//TODO: Should close window; currently doesn't do anything
			}
			System.out.println(solve);//TODO: remove after testing
		}
	}
	
	
	/* Supposed to indicate to the Board class whether or not the user wants to solve, doesn't work*/
	public boolean getToSolve(){//TODO: Doesn't have intended functionality: has to be accessed by the Board class after every attempt to solve..currently doesn't work
		System.out.println(solve);
		return solve;
	}
	
	public JFrame getJFrame() {
		return window;
	}
}
