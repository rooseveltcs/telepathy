package telepathy;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/* GuessTable.java
 * @ Mia Vanderwilt
 * @ 06/18/14
 * <p>
 * A JPanel on the Board GUI that displays each guess that the user makes and whether or not
 * that guess was accurate. Also provides a visual representation for each guessed tile. Does not 
 * require interaction with the user (beyond the popUp).
 * Contains an inner class DepictTile for the buttons on the guess table that prompts the popUp.
 */
public class GuessTable {
	
	private static Tile SECRETSQUARE;//Tile picked by program in Board class
	
	private JPanel gTable;
	private int numG;//NUMBER OF GUESSES
	private JButton[] buttons;//keeps track of which buttons to add guess to
	
	private static Font font;//messing around; same font as used in Board class
	
	/* Constructor
	 * Initializes the JPanel
	 * @param Tile sS the secret square selected within the board class; used to declare whether a guess is right or wrong.
	 * @param Font f the font used in the Board class.
	 */
	public GuessTable(Tile sS, Font f){
		
		font = f;
		SECRETSQUARE = sS;
		numG = 0;
		
		gTable = new JPanel(new GridLayout(16, 2));
		gTable.setSize(150, 600);
		
		JButton gs = new JButton("Guess");
		gs.setFont(font);
		gTable.add(gs);
		JButton yn = new JButton("Yes/No");
		yn.setFont(font);
		gTable.add(yn);
		//TODO: Only allows for 15 guesses; an excess does not show on the GuessTable
		buttons = new JButton[30];
		for (int i = 0; i < 30; i++){
			JButton b = new JButton();
			b.setFont(font);
			buttons[i] = b;
			gTable.add(b);
		}
	}
	
	/* makeGuess()
	 * Takes the guessed Tile and shows the location of the tile in the first column of the JPanel
	 * and then shows the accuracy ("Y" or "N") in the second column of the JPanel.
	 * In addition, it adds a MouseListener to each filled out row to remind the user where and what 
	 * Tile they guessed.
	 * <p>
	 * Used in the Board class; invoked by the TileListener. When a tile is pressed the Tile is 
	 * passed as parameter to this method.
	 * 
	 * @param Tile guess is the Tile whose JButton was pressed
	 */
	public void makeGuess(Tile guess){
		
		if (numG <= 14) {
			//Puts text on buttons within the guess table, depends how filled out the table is
			JButton g = buttons[numG * 2]; 
			g.setText(guess.toString());//In GUESS COLUMN
			g.addMouseListener(new DepictTile (guess));
			buttons[numG * 2 + 1].setText(accuracy(guess));//In Y/N COLUMN
		} else {
			JButton g = new JButton(guess.toString());
			JButton a = new JButton(accuracy(guess));
			g.add(g);//TODO: Doesn't appear on GUI because of the GridLayout of the panel.
			g.add(a);
		}
		numG++;
	}
	
	/* DepictTile.java
	 * 
	 * Used in the GuessTable to show the user what color, symbol, and relative location of the
	 * Tile they have guessed are. When a user mouses over the String representation of the Tile 
	 * (row column) in panel, a popUp shows up with an enlarged image of that Tile's JButton. popUp
	 * closes when mouse exits the string representation.
	 */
	private class DepictTile extends MouseInputAdapter {
		
		private Tile tile;
		private JFrame popUp;
		
		private DepictTile (Tile t ){
			tile = t;
			popUp = new JFrame();
			popUp.setSize(100, 100);
		}
		
		public void mouseEntered(MouseEvent event){
			popUp.setLocationRelativeTo(tile.getButton());//Tells user where in the Grid the Tile is
			popUp.add(tile.imageRep);
			popUp.setTitle(tile.toString());
			popUp.setVisible(true); 
		}
		public void mouseExited(MouseEvent event){
			popUp.setVisible(false);//Closes popUp when mouse Exits the button on GuessTable
		}
	}
	
	/* accuracy()
	 * If the guessed tile shares ANY of the four characteristics with the secretSquare (instance field)
	 * then the accuracy is "Y" and that is what is displayed on the GuessTable. If NONE of the 
	 * characteristics are the same (color, row, column, symbol) then the accuracy is "N" and that
	 * displays on the GuessTable. The accuracy does not affect game play, but informs the user.
	 * <p>
	 * Used in the makeGuess() method which fills out the GuessTable.
	 *  
	 * @param Tile g represents the guessed tile
	 * @return String which is "Y" or "N" based upon whether the guess is "correct"
	 */
	private String accuracy (Tile g){
		if (g.row.equals(SECRETSQUARE.row)){
			return "Y";
		} else if (g.column == SECRETSQUARE.column){
			return "Y";
		} else if (g.color.equals(SECRETSQUARE.color)){
			return "Y";
		} else if (g.symbol.getName().equals(SECRETSQUARE.symbol.getName())){//symbol.equals(symbol) wasn't working?
			return "Y";
		}
		return "N";
	}
	
	public int getNumG(){
		return numG;
	}
	
	public JPanel getPanel(){
		return gTable;
	}
}